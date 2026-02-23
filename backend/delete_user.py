import mysql.connector
import sys
from db_config import DB_CONFIG

def delete_user(identifier):
    """
    Deletes a user from the PathPilot database by email or ID.
    Because of ON DELETE CASCADE in schema.sql, all related data 
    (saved colleges, events, feedback, etc.) will also be deleted.
    """
    try:
        conn = mysql.connector.connect(**DB_CONFIG)
        cursor = conn.cursor()

        # Check if identifier is ID or Email
        if identifier.isdigit():
            query = "DELETE FROM users WHERE id = %s"
        else:
            query = "DELETE FROM users WHERE email = %s"

        cursor.execute(query, (identifier,))
        rows_affected = cursor.rowcount
        conn.commit()

        if rows_affected > 0:
            print(f"Successfully deleted user '{identifier}' and all their related data.")
        else:
            print(f"No user found with identifier '{identifier}'.")

        cursor.close()
        conn.close()

    except mysql.connector.Error as err:
        print(f"Database Error: {err}")
    except Exception as e:
        print(f"An error occurred: {e}")

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Usage: python delete_user.py <email_or_id>")
        sys.exit(1)
    
    user_to_delete = sys.argv[1]
    
    # Confirmation prompt
    confirm = input(f"Are you sure you want to delete user '{user_to_delete}'? This cannot be undone. (y/n): ")
    if confirm.lower() == 'y':
        delete_user(user_to_delete)
    else:
        print("Deletion cancelled.")
