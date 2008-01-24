<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="propertiesBean" scope="request" type="jetbrains.buildServer.controllers.BasePropertiesBean"/>

<div class="parameter">
  Rake task name: <strong><props:displayValue name="rakeRunner.rake.task.name" emptyValue="not specified"/></strong>
</div>

<div class="nestedParameter">
  <ul style="list-style: none; padding-left: 0; margin-left: 0; margin-top: 0.1em; margin-bottom: 0.1em;">
    <li>Trace: <strong><props:displayCheckboxValue name="rakeRunner.rake.options.trace"/></strong></li>
    <li>Quite: <strong><props:displayCheckboxValue name="rakeRunner.rake.options.quite"/></strong></li>
  </ul>
</div>

<div class="parameter">
  Enable debug messages: <strong><props:displayCheckboxValue name="rakeRunner.debug"/></strong>
</div>