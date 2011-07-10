<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output method="html"/>

    <xsl:template match="/">
        <html>
            <head>
                <title>stylesheet.xsl</title>
            </head>
            <body>
                <xsl:apply-templates/>
            </body>
        </html>
    </xsl:template>

    <xsl:template match="salary">
        <tr>
            <td>Зарплата</td>
            <td>
                <xsl:choose>
                    <xsl:when test="not(./minimum/text()) and not(./maximum/text())">
                        <xsl:text>Не указана</xsl:text>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:if test="./minimum/text()">
                            <xsl:text>от </xsl:text><xsl:value-of select="./minimum/text()"/>
                        </xsl:if>
                        <xsl:if test="./maximum/text()">
                            <xsl:text> до </xsl:text><xsl:value-of select="./maximum/text()"/>
                        </xsl:if>
                    </xsl:otherwise>
                </xsl:choose>
            </td>
        </tr>
    </xsl:template>

    <xsl:template match="vacancy">
        <table>
            <tbody>
                <xsl:apply-templates/>
            </tbody>
        </table>
    </xsl:template>
    
    <xsl:template match="description">
        <tr>
            <td>Описание</td>
            <td><xsl:value-of select="."/></td>
        </tr>
    </xsl:template>
    
    <xsl:template match="//vacancyName">
        <tr>
            <td>Название вакансии</td>
            <td>
                <a>
                    <xsl:attribute name="href">
                        <xsl:text>http://</xsl:text>
                        <xsl:value-of select="@url"/>
                    </xsl:attribute>
                    <xsl:value-of select="./text()"/>
                </a>
            </td>
        </tr>
    </xsl:template>
    
    <xsl:template match="//companyName">
        <tr>
            <td>Компания:</td>
            <td>
                <b><a>
                    <xsl:attribute name="href">
                        <xsl:text>http://</xsl:text>
                        <xsl:value-of select="@companyUrl"/>
                    </xsl:attribute>
                    <xsl:value-of select="./text()"/>
                </a></b>
            </td>
        </tr>
    </xsl:template>
    
    <xsl:template match="//vacancyCity">
        <tr>
            <td>Регион вакансии</td>
            <td><xsl:value-of select="./text()"/></td>
        </tr>
    </xsl:template>

</xsl:stylesheet>