<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="kkjsp" uri="http://www.keikai.io/jsp/kk"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>Application for Leave</title>
    <kkjsp:head/>
</head>
<body>
<div>
    <kkjsp:spreadsheet id="myzss" src="/WEB-INF/books/demo_sample.xlsx"
                       width="1024px" height="768px"
                       maxVisibleRows="100" maxVisibleColumns="20"
                       showToolbar="true" showFormulabar="true" showContextMenu="true" showSheetbar="true" showSheetTabContextMenu="true"/>
</div>
</body>
</html>
