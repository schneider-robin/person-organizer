import psycopg2
from psycopg2.extras import RealDictCursor
import requests

def get_all_from_database():

    try:
        connection = psycopg2.connect(
            user = "postgres",
            password = "postgres",
            host = "127.0.0.1",
            port = "5433",
            database = "person_organizer"
        )
        cursor = connection.cursor(cursor_factory=RealDictCursor)
        
        sql = \
        """
            select * from person_organizer.person
        """
        
        cursor.execute(sql)
        person_list_db = cursor.fetchall()
        
        print(f"person_list_db length: {len(person_list_db)}")
        
        for person in person_list_db:
            firstname = person["firstname"]
            print(firstname)
            
    except (Exception, psycopg2.Error) as error:
        print("Error while fetching data from PostgreSQL", error)

    finally:
        # closing database connection.
        if connection:
            cursor.close()
            connection.close()
            # print("PostgreSQL connection is closed")
        

# ------------------------------------------------------------------------------

def get_all_from_api():

    url = "http://localhost:8080/people/all"

    headers = {
            'Content-type': 'application/json',
        }

    response = requests.get(url = url, headers = headers)

    person_list_api = response.json().get('person_responses');

    print(f"person_list_api length: {len(person_list_api)}")

    for person in person_list_api:
            firstname = person["full_name"]
            print(firstname)
        
# ------------------------------------------------------------------------------
        
if __name__ == "__main__":
    print("---------------------------------")
    get_all_from_database()
    print("---------------------------------")
    get_all_from_api()
    print("---------------------------------")