# Permission Tracker

PermissionTracker is a web application developed to streamline and secure the process of managing user access rights in an organization. The application allows users to request specific access permissions, such as accessing certain resources or functionalities. These requests are then routed to the respective approvers for review and approval.

## Key Features

- User-friendly interface for submitting access permission requests.
- Integration with LDAP for user authentication and retrieval of user information.
- Automated email notifications for request submission, approval, and denial.
- Approval workflow with multiple levels of authorization.
- Detailed logging and audit trail of all permission requests and approvals.

## Technologies Used

- Spring Boot: Backend framework for developing the web application.
- Vaadin: Java framework for building the frontend user interface.
- MariaDB: Database system for storing user and permission data.
- Bootstrap Email: Library for formatting email notifications in a visually appealing manner.
- Git: Version control system for tracking changes in the source code.
- GitHub: Hosting platform for the project repository.

## Getting Started

To run the PermissionTracker application locally, follow these steps:

1. Clone the repository: `git clone https://github.com/your-username/permission-tracker.git`
2. add the necessary application-dev.properties file and edit it urself until every variable is defined.
3. Install the necessary dependencies using Maven: `mvn install`
4. Configure the database connection in the `application-dev.properties` file.
5. Build and run the application: `mvn spring-boot:run`
6. Access the application in your web browser at `http://localhost:8080`.

## Documentation

Please refer to the inline documentation for additional resources related to the PermissionTracker application. This includes user guides, API documentation, and information about the development process.
Also checkout the scientific paper about PermissionTracker written by Fritz Schubert. Reach out to him to read it.

## Contributing

Contributions to the PermissionTracker project are welcome. If you encounter any issues or have suggestions for improvements, please submit them through the GitHub issue tracker.
To contribute to the project it's recommended to set up your own email server and active directory. Better do this by providing your own Windows Server Domain Controller with Active Directory and your own Microsoft Exchange Server.

## License

PermissionTracker is released under the [MIT License](./license.md). You are free to use, modify, and distribute the application in accordance with the terms of the license.

---

Feel free to update this README file with any additional information or instructions specific to your project. Happy coding!
