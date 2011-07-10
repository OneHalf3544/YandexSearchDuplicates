<?xml version="1.0"?>

<!--
    Document   : report.xsl
    Created on : 10 Июль 2011 г., 0:01
    Author     : OneHalf
    Description:
        Purpose of transformation follows.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output method="html"/>
    <xsl:template match="/">
        <html>
            <head>
                <title>Отчет по поиску одинаковых вакансий</title>
            </head>
            <body>
                <h1>Отчет по поиску одинаковых вакансий</h1>
                <table>
                    <tbody>
                        <xsl:apply-templates/>
                    </tbody>
                </table>
            </body>
        </html>
    </xsl:template>

    <xsl:template match="duplicates">
        <tr>
            <xsl:apply-templates />
        </tr>
    </xsl:template>

    <xsl:template match="equalency">
        <td>
            <xsl:apply-templates />%
        </td>
    </xsl:template>

    <xsl:template match="vacancy">
        <a>
            <xsl:attribute name="href">
                <xsl:text>http://</xsl:text>
                <xsl:value-of select="url/text()"/>
            </xsl:attribute>
            <xsl:value-of select="name/text()"/>
        </a>  
    </xsl:template>

    <xsl:template match="name">
        <xsl:value-of select="//text()"/>
    </xsl:template>
</xsl:stylesheet>
