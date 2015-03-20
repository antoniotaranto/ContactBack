<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false"%>
                <div class="header">
                    <c:import url="/Controls/TopMenuControl/${activeModule}"></c:import>
                    <c:import url="/Controls/UserMenuControl"></c:import>
                    <div class="logos">
                        <p>
                            <strong>PORTAL DE SERVIÇOS</strong>
                            <span>CAC DANONE</span>
                        </p>
                        <h1><a href="#" title="Início">Danone</a></h1>
                    </div>
                    <p class="bem-vindo">
                        Olá <strong>${user.name}</strong>, seja bem-vindo.
                    </p>
                </div>
