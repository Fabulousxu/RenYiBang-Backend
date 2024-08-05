import random
from faker import Faker

# Initialize Faker
fake = Faker()

# Generate random service records
service_records = []
for _ in range(100000):
    owner_id = random.randint(1, 100000)  # Assuming owner_id ranges from 1 to 100000
    title = fake.sentence(nb_words=6)[:32].replace('"', '""')  # Ensure title does not exceed 32 characters
    images = " ".join([fake.image_url() for _ in range(random.randint(0, 3))]).replace('"', '""')
    description = fake.text(max_nb_chars=255).replace('"', '""')  # Ensure description does not exceed 255 characters
    price = random.randint(1, 10000) * 100
    created_at = fake.date_time_between(start_date='-1y', end_date='now').strftime('%Y-%m-%d %H:%M:%S')
    max_access = random.randint(1, 10)
    rating = random.randint(0, 100)
    collected = random.randint(0, 10000)
    status = random.choice([0, 2])

    service_records.append(
        f"({owner_id}, \"{title}\", \"{images}\", \"{description}\", {price}, '{created_at}', {max_access}, {rating}, {collected}, {status})")

# Create SQL insert statement
sql_insert_statement = "INSERT INTO service (owner_id, title, images, description, price, created_at, max_access, rating, collected, status) VALUES\n"

# Write to the file in chunks to handle large data
with open("insert_service.sql", "w", encoding="utf-8") as file:
    file.write(sql_insert_statement)
    for i in range(0, len(service_records), 1000):  # Write 1000 records at a time
        chunk = ",\n".join(service_records[i:i + 1000]) + ";\n"
        if i + 1000 < len(service_records):
            chunk = chunk[:-2] + ",\n"
        file.write(chunk)

print("SQL insert script has been generated and saved to insert_service.sql")
