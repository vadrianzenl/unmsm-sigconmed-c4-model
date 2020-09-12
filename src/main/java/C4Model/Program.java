package C4Model;

import com.structurizr.Workspace;
import com.structurizr.api.StructurizrClient;
import com.structurizr.model.*;
import com.structurizr.view.*;

import java.util.stream.Collectors;

public class Program {
    private static final long WORKSPACE_ID = 58312;
    private static final String API_KEY = "6aef53fd-eef8-4111-92bc-16e52d892926";
    private static final String API_SECRET = "86c979aa-be70-4f80-86f9-749d70369518";

    public static void main(String[] args) throws Exception {
        StructurizrClient structurizrClient = new StructurizrClient(API_KEY, API_SECRET);
        Workspace workspace = new Workspace("Sigconmed", "Sigconmed - C4 Model");
        Model model = workspace.getModel();

        SoftwareSystem sigconmedSystem = model.addSoftwareSystem("SIGCONMED", "Permite a los pacientes consultar información de sus citas médidas y datos personales.");
        SoftwareSystem mobileAppSystem = model.addSoftwareSystem("Mobile App", "Permite a los pacientes consultar información de sus citas médidas y datos personales.");
        SoftwareSystem apmSystem = model.addSoftwareSystem("APM New Relic", "Brinda el monitoreo de la aplicación a través de alertas y métricas.");
        SoftwareSystem elkSystem = model.addSoftwareSystem("ELK", "Stack de herramientas para gestión de Logs de la aplicación.");
        SoftwareSystem springCloudSystem = model.addSoftwareSystem("Spring Cloud", "Stack de herramientas para la configuración de la aplicación.");

        Person paciente = model.addPerson("Paciente", "Paciente de ESSALUD.");
        Person administrador = model.addPerson("Administrador", "Empleado de ESSALUD.");

        mobileAppSystem.addTags("Mobile App");
        apmSystem.addTags("APM New Relic");
        elkSystem.addTags("ELK");
        springCloudSystem.addTags("Spring Cloud");

        paciente.uses(sigconmedSystem, "Realiza consultas de citas médidas y datos personales.");
        paciente.uses(mobileAppSystem, "Realiza consultas de citas médidas y datos personales.");
        administrador.uses(sigconmedSystem, "Usa");
        administrador.uses(apmSystem, "Monitorea el estado de la aplicación");
        administrador.uses(elkSystem, "Revisa los logs de la aplicación");

        sigconmedSystem.uses(apmSystem, "Envía datos para monitoreo");
        sigconmedSystem.uses(elkSystem, "Envía los logs de la aplicación");
        sigconmedSystem.uses(springCloudSystem, "Uso de herramientas para la configuración");
        mobileAppSystem.uses(sigconmedSystem, "Usa");

        ViewSet viewSet = workspace.getViews();

        // 1. Diagrama de Contexto
        SystemContextView contextView = viewSet.createSystemContextView(sigconmedSystem, "Contexto", "Diagrama de contexto - Sigconmed");
        contextView.setPaperSize(PaperSize.A4_Landscape);
        contextView.addAllSoftwareSystems();
        contextView.addAllPeople();
        contextView.enableAutomaticLayout();

        Styles styles = viewSet.getConfiguration().getStyles();
        styles.addElementStyle(Tags.PERSON).background("#0a60ff").color("#ffffff").shape(Shape.Person);
        styles.addElementStyle("Mobile App").background("#29c732").color("#ffffff").shape(Shape.MobileDevicePortrait);
        styles.addElementStyle("APM New Relic").background("#90714c").color("#ffffff").shape(Shape.RoundedBox);
        styles.addElementStyle("ELK").background("#a5cdff").color("#ffffff").shape(Shape.RoundedBox);
        styles.addElementStyle("Spring Cloud").background("#a5cdff").color("#ffffff").shape(Shape.RoundedBox);

        // 2. Diagrama de Contenedores
        Container webApplication = sigconmedSystem.addContainer("Aplicación Web", "Permite a los pacientes consultar sus datos personales, programar sus citas médicas y visualizar sus exámenes médicos.", "VueJS, nginx port 8081");
        Container restApi = sigconmedSystem.addContainer("API Gateway", "Api Gateway.", "Netflix Zuul, tomcat port 8090");
        Container microServiceSecurity = sigconmedSystem.addContainer("Microservice Security", "Microservice Security.", "Sprint boot");
        Container microServicePatient = sigconmedSystem.addContainer("Microservice Patient", "Microservice Patient.", "Sprint boot");
        Container microServiceDoctors = sigconmedSystem.addContainer("Microservice Doctors", "Microservice Doctors.", "Sprint boot");
        Container microServiceSpecialities = sigconmedSystem.addContainer("Microservice Specialities", "Microservice Specialities.", "Sprint boot");
        Container microServiceExam = sigconmedSystem.addContainer("Microservice Exam", "Microservice Exam.", "Sprint boot");
        Container databaseSecurity = sigconmedSystem.addContainer("Base de Datos Security", "Base de datos Security.", "Mysql 3306");
        Container databasePatient = sigconmedSystem.addContainer("Base de Datos Patient", "Base de datos Patient.", "Mysql 3306");
        Container databaseDoctors = sigconmedSystem.addContainer("Base de Datos Doctors", "Base de datos Doctors.", "Mysql 3306");
        Container databaseSpecialities = sigconmedSystem.addContainer("Base de Datos Specialities", "Base de datos Specialities.", "Mysql 3306");
        Container databaseExam = sigconmedSystem.addContainer("Base de Datos Exam", "Base de datos Exam.", "Mysql 3306");
        Container messageBus = sigconmedSystem.addContainer("Axon Server", "Transporte de eventos del dominio.", "AxonServer");

        webApplication.addTags("WebApp");
        restApi.addTags("API");
        microServiceSecurity.addTags("Microservice Security");
        microServicePatient.addTags("Microservice Patient");
        microServiceDoctors.addTags("Microservice Doctors");
        microServiceSpecialities.addTags("Microservice Specialities");
        microServiceExam.addTags("Microservice Exam");
        databaseSecurity.addTags("Database Security");
        databasePatient.addTags("Database Patient");
        databaseDoctors.addTags("Database Doctors");
        databaseSpecialities.addTags("Database Specialities");
        databaseExam.addTags("Database Exam");
        messageBus.addTags("MessageBus");

        paciente.uses(webApplication, "Usa", "https 443");
        webApplication.uses(restApi, "Usa", "https 443");
        microServiceSecurity.uses(restApi, "Usa", "https 443");
        microServiceSecurity.uses(messageBus, "Usa");
        microServicePatient.uses(restApi, "Usa", "https 443");
        microServicePatient.uses(messageBus, "Usa");
        microServiceDoctors.uses(restApi, "Usa", "https 443");
        microServiceDoctors.uses(messageBus, "Usa");
        microServiceSpecialities.uses(restApi, "Usa", "https 443");
        microServiceSpecialities.uses(messageBus, "Usa");
        microServiceExam.uses(restApi, "Usa", "https 443");
        microServiceExam.uses(messageBus, "Usa");
        microServiceSecurity.uses(databaseSecurity, "Usa", "jdbc 3306");
        microServicePatient.uses(databasePatient, "Usa", "jdbc 3306");
        microServiceDoctors.uses(databaseDoctors, "Usa", "jdbc 3306");
        microServiceSpecialities.uses(databaseSpecialities, "Usa", "jdbc 3306");
        microServiceExam.uses(databaseExam, "Usa", "jdbc 3306");
        restApi.uses(messageBus, "Usa");
        restApi.uses(apmSystem, "Usa", "https 443");
        mobileAppSystem.uses(restApi, "Usa");

        styles.addElementStyle("WebApp").background("#9d33d6").color("#ffffff").shape(Shape.WebBrowser).icon("data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHZpZXdCb3g9Ii0xMS41IC0xMC4yMzE3NCAyMyAyMC40NjM0OCI+CiAgPHRpdGxlPlJlYWN0IExvZ288L3RpdGxlPgogIDxjaXJjbGUgY3g9IjAiIGN5PSIwIiByPSIyLjA1IiBmaWxsPSIjNjFkYWZiIi8+CiAgPGcgc3Ryb2tlPSIjNjFkYWZiIiBzdHJva2Utd2lkdGg9IjEiIGZpbGw9Im5vbmUiPgogICAgPGVsbGlwc2Ugcng9IjExIiByeT0iNC4yIi8+CiAgICA8ZWxsaXBzZSByeD0iMTEiIHJ5PSI0LjIiIHRyYW5zZm9ybT0icm90YXRlKDYwKSIvPgogICAgPGVsbGlwc2Ugcng9IjExIiByeT0iNC4yIiB0cmFuc2Zvcm09InJvdGF0ZSgxMjApIi8+CiAgPC9nPgo8L3N2Zz4K");
        //styles.addElementStyle("API").background("#929000").color("#ffffff").shape(Shape.RoundedBox).icon("https://dotnet.microsoft.com/static/images/redesign/downloads-dot-net-core.svg?v=U_8I9gzFF2Cqi5zUNx-kHJuou_BWNurkhN_kSm3mCmo");
        //styles.addElementStyle("Worker").icon("https://dotnet.microsoft.com/static/images/redesign/downloads-dot-net-core.svg?v=U_8I9gzFF2Cqi5zUNx-kHJuou_BWNurkhN_kSm3mCmo");
        //styles.addElementStyle("Database").background("#ff0000").color("#ffffff").shape(Shape.Cylinder).icon("https://4.bp.blogspot.com/-5JVtZBLlouA/V2LhWdrafHI/AAAAAAAADeU/_3bo_QH1WGApGAl-U8RkrFzHjdH6ryMoQCLcB/s200/12cdb.png");
        //styles.addElementStyle("MessageBus").width(850).background("#fd8208").color("#ffffff").shape(Shape.Pipe).icon("https://www.rabbitmq.com/img/RabbitMQ-logo.svg");

        ContainerView containerView = viewSet.createContainerView(sigconmedSystem, "Contenedor", "Diagrama de contenedores - Sigconmed");
        contextView.setPaperSize(PaperSize.A4_Landscape);
        containerView.addAllElements();
        containerView.enableAutomaticLayout();

        // 3. Diagrama de Componentes
        Component transactionController = restApi.addComponent("Transactions Controller", "Allows users to perform transactions.", "Spring Boot REST Controller");
        Component signinController = restApi.addComponent("SignIn Controller", "Allows users to sign in to the Internet Banking System.", "Spring Boot REST Controller");
        Component accountsSummaryController = restApi.addComponent("Accounts Controller", "Provides customers with an summary of their bank accounts.", "Spring Boot REST Controller");
        Component securityComponent = restApi.addComponent("Security Component", "Provides functionality related to signing in, changing passwords, etc.", "Spring Bean");
        Component mainframeBankingSystemFacade = restApi.addComponent("Mainframe Banking System Facade", "A facade onto the mainframe banking system.", "Spring Bean");

        restApi.getComponents().stream()
                .filter(c -> "Spring Boot REST Controller".equals(c.getTechnology()))
                .collect(Collectors.toList())
                .forEach(c -> webApplication.uses(c, "Uses", "HTTPS"));

        signinController.uses(securityComponent, "Uses");
        accountsSummaryController.uses(mainframeBankingSystemFacade, "Uses");
        securityComponent.uses(databaseSecurity, "Reads from and writes to", "JDBC");

        ComponentView componentViewForRestApi = viewSet.createComponentView(restApi, "Components", "The components diagram for the REST API");
        componentViewForRestApi.setPaperSize(PaperSize.A4_Landscape);
        componentViewForRestApi.addAllContainers();
        componentViewForRestApi.addAllComponents();
        componentViewForRestApi.add(paciente);
        componentViewForRestApi.enableAutomaticLayout();

        structurizrClient.unlockWorkspace(WORKSPACE_ID);
        structurizrClient.putWorkspace(WORKSPACE_ID, workspace);
    }
}
