# Crime Pattern Detection System

A desktop application built with Java Swing for managing and analyzing crime data. This system helps law enforcement agencies track crimes, manage evidence, and generate analytical reports.

## Features

- **Crime Management**: Add, edit, and track crime records with detailed information
- **Evidence Tracking**: Manage physical and digital evidence for each case
- **Analytics Dashboard**: View crime statistics with interactive charts
- **Search & Filter**: Advanced search capabilities across multiple criteria
- **Report Generation**: Export data to CSV and HTML formats
- **Case Updates**: Track investigation progress with timestamped notes

## Tech Stack

- Java 23
- Swing (GUI)
- MySQL (Database)
- JFreeChart (Data visualization)
- FlatLaf (Modern UI theme)
- Maven (Build tool)

## Getting Started

### Prerequisites

- JDK 23 or higher
- MySQL 8.0+
- Maven 3.6+

### Installation

1. Clone the repository
```bash
git clone https://github.com/PathanWasim/crime-pattern-detection-system.git
cd crime-pattern-detection-system
```

2. Set up the database
```bash
mysql -u root -p < database_schema.sql
```

3. Update database credentials in `src/main/resources/application.properties`

4. Build and run
```bash
mvn clean install
mvn exec:java
```

Or use the launcher script:
```bash
./launch.bat
```

## Usage

**Default Login:**
- Username: `admin`
- Password: `admin123`

The application provides an intuitive interface for:
- Recording new crimes with priority levels
- Searching existing records by location, type, or status
- Viewing analytics with charts and graphs
- Managing evidence and case updates
- Generating reports for stakeholders

## Database Schema

The system uses four main tables:
- `admin` - User authentication
- `crime` - Main crime records
- `crime_evidence` - Evidence tracking
- `crime_updates` - Case progress notes

## Project Structure

```
src/main/java/com/crimedetect/
├── auth/          # Login and authentication
├── db/            # Database operations
├── model/         # Data models
├── ui/            # User interface panels
├── utils/         # Helper utilities
└── App.java       # Main application
```

## Testing

Run tests with:
```bash
mvn test
```

## Contributing

Feel free to submit issues and pull requests. For major changes, please open an issue first to discuss what you would like to change.

## License

This project is open source and available under the MIT License.