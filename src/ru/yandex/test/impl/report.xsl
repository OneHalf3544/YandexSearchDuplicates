<?xml version="1.0"?>

<!--
    Document   : report.xsl
    Created on : 10 Июль 2011 г., 0:01
    Author     : OneHalf
    Description:
        Стиль для xml-файла со списком дублирующихся вакансий.
        Используется для генерации отчета
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output method="html"/>
    <xsl:template match="/">
        <html>
            <head>
                <title>Отчет по поиску одинаковых вакансий</title>
                <style type="text/css">
                    div.vacancy {
                        padding: 10px;
                    }
                    table {
                        border-collapse: collapse;
                    }
                    td, th { 
                        border: 1px solid black;
                        padding: 20px;
                    }
                </style>
            </head>
            <body>
                <h1>Отчет по поиску одинаковых вакансий</h1>
                <table>
                    <tbody>
                        <tr><th>Схожесть</th><th>Вакансии</th></tr>
                        <xsl:for-each select="/root/duplicates">
                            <xsl:sort order="descending" select="equalency"/>
                            <tr><td><xsl:apply-templates select="equalency" /></td>
                                <td><xsl:apply-templates select="vacancy" /></td>
                            </tr>
                        </xsl:for-each>
                    </tbody>
                </table>
            </body>
        </html>
    </xsl:template>

    <xsl:template match="equalency"><xsl:value-of select="." />%</xsl:template>

    <xsl:template match="vacancy">
        <div class="vacancy">
            <span>Вакансия: </span><a href="{url/text()}">
                <xsl:value-of select="name/text()"/>
            </a><br />
            <span>Компания: </span><a href="{company/url/text()}">
                <xsl:value-of select="company/name/text()"/>
            </a><br />
            <span>Регион: <xsl:value-of select="city/text()"/></span><br />
            <span>Зарплата: <xsl:value-of select="salary/text()"/></span>
        </div>
    </xsl:template>
</xsl:stylesheet>
