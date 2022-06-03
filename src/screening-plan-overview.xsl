<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" version="1.0">
    <xsl:output method="html"/>
    <!-- Insert the XSLT Stylesheet here -->



    <xsl:template match="screening-plan">
        <html>
            <head>
                <title> Screening Plan - Overview</title>
                <style>
body {
    font-family: Verdana, sans-serif;
    background-color: #FFFED9;
}
table {
    font-family: Verdana, sans-serif;
    border-collapse: collapse;
    width: 50em;
    border: 1px solid #080808;
    background-color: #FFF4B0;
}

tr {
    border: 2px solid #000000;
    border-width: 1px 0;
}

td, th {
    border: 1px solid #080808;
    padding: 8px;    
    vertical-align: top;
}


th {
    padding-top: 2px;
    border: 1px solid #080808;
    padding-bottom: 2px;
    text-align: center;
    background-color: #FFCB87;
    color: 'white';
    vertical-align: top;
}
</style>
            </head>
            <body>
                <h1>Overview on screened films</h1>
                  <hr/>
                <xsl:apply-templates select="//movies/movie"/>
            </body>
        </html>
    </xsl:template> 
	
	<!-- Insert additional templates here -->





    <xsl:template match="//movies/movie">
        <xsl:call-template name="callmoviesname"></xsl:call-template>
    </xsl:template>

    <xsl:template name="callmoviesname">

        <h2>
        <xsl:value-of select="name"></xsl:value-of>
        </h2>
        <b>
           Genre:
        </b> <xsl:value-of select="genre"></xsl:value-of>
        <br>
            <b>Roles: </b>
            <br></br>
            <xsl:apply-templates select="roles | support"/>

        </br>






<table>
    <tr>
        <td>
            <xsl:call-template name="populateScreenings">
                <xsl:with-param name="mid" select="@mid"></xsl:with-param>
            </xsl:call-template>
        </td>
    </tr>
</table>


    </xsl:template>

    <xsl:template name="populateScreenings">
<xsl:param name="mid"></xsl:param>



        <xsl:for-each select="//cinema/movie">
            <xsl:sort select="following-sibling::*/@announcement" case-order="upper-first" order="ascending" ></xsl:sort>

            <xsl:if test="@movie_ref = $mid">

                <h3> Showings at Cinema <xsl:value-of select="count(../preceding-sibling::*) + 1"/> </h3>


                <xsl:if test="(not(following-sibling::*[1]/name()='info'))">
                    <table>
                        <tbody>
                            <tr>
                                <th>
                                    No info available on screenings!
                                </th>
                            </tr>
                        </tbody>
                    </table>


</xsl:if>

                <xsl:if test="(following-sibling::*[1]/name()='info')">

                    <b>Announced on: </b> <xsl:value-of select="following-sibling::*/@announcement"></xsl:value-of>

                    <table>
                        <tbody>
                            <tr>
                                <th>
                                    Premiere Showing on <xsl:value-of select="following-sibling::*/premiere-date"></xsl:value-of>
                                    in Room <xsl:value-of select="following-sibling::*/premiere-room"></xsl:value-of>
                                </th>
                            </tr>
                            <tr>
                                <td>
                                    <b>Regular Showings:</b> Shown on <xsl:value-of select="following-sibling::*/screening-date"></xsl:value-of>

                                    in

                                    <xsl:for-each select="following-sibling::*[1]/room">
                                        Room <xsl:value-of select="."></xsl:value-of>,

                                    </xsl:for-each>

                                </td>

                            </tr>
                            <tr>
                                <td>
                                    <b>Shown in: </b> <xsl:value-of select="count(following-sibling::*[1]/room) + 1"></xsl:value-of> room(s)
                                </td>


                            </tr>
                        </tbody>
                    </table>



                </xsl:if>



            </xsl:if>

        </xsl:for-each>


    </xsl:template>

<xsl:template match="main | support">


    <xsl:variable name="var2" select="name(.)"></xsl:variable>
    <xsl:variable name="var" select="@actor_ref"></xsl:variable>
    <xsl:for-each select="@actor_ref">



        <xsl:if test="$var2 = 'main'">
            <b>Main: </b>
        </xsl:if>
        <xsl:if test="$var2 = 'support'">
            <b>Support: </b>
        </xsl:if>


    </xsl:for-each>

   <xsl:for-each select="//actors/actor">

       <xsl:if test="$var = @aid">
           <xsl:value-of select="."></xsl:value-of>
           (Age: <xsl:value-of select="year-from-date(current-date()) - year-from-date(@birth_date)"></xsl:value-of>)
          (Country: <xsl:value-of select="@birth_country"></xsl:value-of>)
           <xsl:value-of select="/screening-plan//movies/*"></xsl:value-of>
           <br></br>
       </xsl:if>

   </xsl:for-each>


</xsl:template>


</xsl:stylesheet>
