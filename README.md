# Hotel Management System API

## Overview

I made this project as my first java application for the uni project.
The Hotel Management System API provides endpoints to manage rooms and bookings within a hotel.

## Endpoints

### Rooms

#### Get All Rooms

Returns a list of all rooms available in the hotel.

- **URL:** `/room/all`
- **Method:** `GET`
- **Response:** List of rooms in JSON format.

##### Example

```
GET /room/all HTTP/1.1
```

#### Get Room by ID

Retrieves a specific room by its unique ID.

- **URL:** `/room/{id}`
- **Method:** `GET`
- **Parameters:**
  - `id`: The ID of the room to retrieve.
- **Response:** Details of the room in JSON format.

##### Example

```
GET /room/123 HTTP/1.1
```

#### Get Booked Terms for Room

Returns a list of booked terms for a specific room.

- **URL:** `/room/booked/{id}`
- **Method:** `GET`
- **Parameters:**
  - `id`: The ID of the room.
- **Response:** List of booked terms in JSON format.

##### Example

```
GET /room/booked/123 HTTP/1.1
```

#### Get Free Rooms for Term

Returns a list of rooms available for booking within a specified date range.

- **URL:** `/room/free`
- **Method:** `GET`
- **Parameters:**
  - `startDate`: The start date of the term.
  - `endDate`: The end date of the term.
- **Response:** List of available rooms in JSON format.

##### Example

```
GET /room/free?startDate=2024-04-01&endDate=2024-04-05 HTTP/1.1
```

#### Add New Room

Adds a new room to the hotel.

- **URL:** `/room/add`
- **Method:** `POST`
- **Request Body:** Room details in JSON format.
- **Response:** Details of the added room in JSON format.

##### Example

```
POST /room/add HTTP/1.1
Host: your-base-url.com
Content-Type: application/json

{
  "description": "Sample Room",
  "capacity": 2,
  "price": 100,
  "floor": 1,
  "number": 101,
  "photos": [
            {
                "fileName": "example1.jpg",
                "url": "https://example.com/example1.jpg"
            },
            {
                "fileName": "example2.jpg",
                "url": "https://example.com/example2.jpg"
            }
        ]
}
```

#### Update Room

Updates the details of an existing room.

- **URL:** `/room/{id}`
- **Method:** `PUT`
- **Parameters:**
  - `id`: The ID of the room to update.
- **Request Body:** Updated room details in JSON format.
- **Response:** Details of the updated room in JSON format.

##### Example

```
PUT /room/123 HTTP/1.1
Host: your-base-url.com
Content-Type: application/json

{
  "description": "Updated Room",
  "capacity": 3,
  "price": 500,
  "floor": 1,
  "number": 123,
  "photos": [
            {
                "fileName": "example1.jpg",
                "url": "https://example.com/example1.jpg"
            },
            {
                "fileName": "example2.jpg",
                "url": "https://example.com/example2.jpg"
            },
            {
                "fileName": "example3.jpg",
                "url": "https://example.com/example3.jpg"
            }
        ]
}
```

#### Delete Room

Deletes a room from the hotel.

- **URL:** `/room/{id}`
- **Method:** `DELETE`
- **Parameters:**
  - `id`: The ID of the room to delete.
- **Response:** Confirmation message.

##### Example

```
DELETE /room/123 HTTP/1.1
```

### Bookings

#### Get All Bookings

Returns a list of all bookings made in the hotel.

- **URL:** `/booking/all`
- **Method:** `GET`
- **Response:** List of bookings in JSON format.

##### Example

```
GET /booking/all HTTP/1.1
```

#### Add New Booking

Adds a new booking to the hotel.

- **URL:** `/booking/add`
- **Method:** `POST`
- **Request Body:** Booking details in JSON format.

##### Example

```
POST /booking/add HTTP/1.1
Content-Type: application/json

{
    "guest": {
            "firstName": "Jan",
            "email": "example@example.com",
            "phoneNumber": "123456789",
            "lastName": "Kowalski"
        },
    "roomId": 123,
    "checkInDate": "2024-04-01",
    "checkOutDate": "2024-04-05",
    "numberOfGuests": 2
}
```

#### Get Booking by ID

Retrieves a specific booking by its unique ID.

- **URL:** `/booking/{id}`
- **Method:** `GET`
- **Parameters:**
  - `id`: The ID of the booking to retrieve.
- **Response:** Details of the booking in JSON format.

##### Example

```
GET /booking/123 HTTP/1.1
```

#### Update Booking

Updates the details of an existing booking.

- **URL:** `/booking/{id}`
- **Method:** `PUT`
- **Parameters:**
  - `id`: The ID of the booking to update.
- **Request Body:** Updated booking details in JSON format.

##### Example

```
PUT /booking/123 HTTP/1.1
Content-Type: application/json
{
    "guest": {
            "firstName": "Jan",
            "email": "example@example.com",
            "phoneNumber": "123456789",
            "lastName": "Kowalski"
        },
    "roomId": 123,
    "checkInDate": "2024-04-02",
    "checkOutDate": "2024-04-06",
    "numberOfGuests": 2
}
```

#### Delete Booking

Deletes a booking from the hotel.

- **URL:** `/booking/{id}`
- **Method:** `DELETE`
- **Parameters:**
  - `id`: The ID of the booking to delete.
- **Response:** Confirmation message.

##### Example

```
DELETE /booking/123 HTTP/1.1
```
