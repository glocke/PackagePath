<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="json">
	<ul class="master-filter-ul">
		<li class="master-filter-li">
			<div class="master-filter-list-content">
				<h4 class="filter-list-title">Inbound / Outbound</h4>
				<ul class="filter-list-ul">
					<xsl:for-each select="destination/item">
						<li><label id="f_{name}_label" for="f_{name}" class="btn active filter-checkbox-label"><input id="f_{name}" type="checkbox" value="{name}" class="filter-checkbox filter-direction" checked="true"/><xsl:value-of select="label"/></label></li>
 					</xsl:for-each>
 				</ul>
			</div>
		</li>
		<li class="master-filter-li">
			<div class="master-filter-list-content">
				<h4 class="filter-list-title">Carrier</h4>
				<ul class="filter-list-ul">
					<xsl:for-each select="carrier/item">
						<li><label id="f_{name}_label" for="f_{name}" class="btn active filter-checkbox-label"><input id="f_{name}" type="checkbox" value="{name}" class="filter-checkbox filter-carrier" checked="true"/><xsl:value-of select="label"/></label></li>
					</xsl:for-each>
				</ul>
			</div>
		</li>
		<li class="master-filter-li">
			<div class="master-filter-list-content">
				<h4 class="filter-list-title">Days</h4>
				<ul class="filter-list-ul">
					<xsl:for-each select="days/item">
						<li><label id="f_{name}_label" for="f_{name}" class="btn active filter-checkbox-label"><input id="f_{name}" type="checkbox" value="{name}" class="filter-checkbox filter-day" checked="true"/><xsl:value-of select="label"/></label></li>
					</xsl:for-each>
				</ul>
			</div>
		</li>
		<li class="master-filter-li">
			<div class="master-filter-list-content master-filter-list-content-last">
				<h4 class="filter-list-title">Tracking #</h4>
				<ul class="filter-list-ul">
					<xsl:for-each select="numbers/item">
						<li><label id="f_{name}_label" for="f_{name}" class="btn active filter-checkbox-label"><input id="f_{name}" type="checkbox" value="{name}" class="filter-checkbox filter-number" checked="true"/><xsl:value-of select="label"/></label></li>
					</xsl:for-each>
				</ul>
			</div>
		</li>
	</ul>
	</xsl:template>
</xsl:stylesheet>