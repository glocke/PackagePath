<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="json/ContactCards">

		<h2>Contact cards</h2>
		<ul>
			<xsl:for-each select="item">

				<li><a href="mailto:{E-mail}"><xsl:value-of select="Name" /></a></li>

			</xsl:for-each>
		</ul>

	</xsl:template>
</xsl:stylesheet>