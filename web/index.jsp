<!DOCTYPE html>
<html>
<head>
  <title>Optimized Firewall Anomaly Resolution  web service</title>
</head>
<body>
<h2>Optimized Firewall Anomaly Resolution RESTful Web Service</h2>
<h4>DP2 - Special Project</h4>

<p>
  The optimized firewall anomaly resolution web service is a simple web service that performs anomaly resolution
  based on a well defined algorithm that edit the firewall rules and remove some anomaly types in an automatic way
  so that we can limit the involvement of the network administrator. There are some types of anomalies that requires the
  network administrator.
</p>
<p>
  The main resource of the service is the collection resource
  named <a href="rest/optimizer">ServiceInputs</a>
  which represents the (initially empty) collection of optimizer object which contains
  items of class ServiceInput. By POST-ing to this resource it is possible to create
  new ServiceInput objects as sub-resources. The ids of these resources
  are decided by the service.
  Resources representing single ServiceInput objects can be read,
  updated and deleted by means of get, put and delete methods.
</p>
<p>Visit <a href="https://eclipse-ee4j.github.io/jersey/">Project Jersey website</a>
  for more information on Jersey!
</p>
<p><a href="_swagger-ui/index.html?url=http://localhost:8080/Optimized_Firewall_Anomaly_Resolution_war_exploded/rest/swagger.json">Swagger documentation</a>
</p>
<footer>Mohamed Mamdouh Tourab</footer>

</body>
</html>