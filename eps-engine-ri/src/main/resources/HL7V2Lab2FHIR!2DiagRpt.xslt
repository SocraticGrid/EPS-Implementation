<?xml version="1.0" encoding="UTF-8"?>
<!--
Copyright 2015 Cognitive Medical Systems, Inc (http://www.cognitivemedciine.com).

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
-->

<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:vmf="http://www.altova.com/MapForce/UDF/vmf" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:fn="http://www.w3.org/2005/xpath-functions" exclude-result-prefixes="vmf xs fn">
	<xsl:template name="vmf:vmf1_inputtoresult">
		<xsl:param name="input" select="()"/>
		<xsl:choose>
			<xsl:when test="$input='F'">
				<xsl:value-of select="'final'"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="'unknown'"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<xsl:output method="xml" encoding="UTF-8" indent="yes"/>
	<xsl:template match="/">
		<xsl:variable name="var1_HLMessage" as="node()?" select="HL7Message"/>
		<xsl:variable name="var11_resultof_map" as="node()?">
			<xsl:for-each select="$var1_HLMessage">
				<xsl:variable name="var2_resultof_cast" as="xs:double" select="xs:double(xs:decimal('3'))"/>
				<xsl:variable name="var3_resultof_cast" as="xs:double" select="xs:double(xs:decimal('2'))"/>
				<xsl:variable name="var4_resultof_cast" as="xs:double" select="xs:double(xs:decimal('5'))"/>
				<xsl:variable name="var5_resultof_cast" as="xs:double" select="xs:double(xs:decimal('1'))"/>
				<xsl:variable name="var6_resultof_cast" as="xs:string" select="fn:string(OBR/OBR.7/OBR.7.1)"/>
				<xsl:variable name="var7_resultof_substring_before" as="xs:string" select="fn:substring-before($var6_resultof_cast, '-')"/>
				<xsl:variable name="var8_resultof_substring_after" as="xs:string" select="fn:substring-after($var6_resultof_cast, '-')"/>
				<xsl:variable name="var9_resultof_substring" as="xs:string" select="fn:substring($var7_resultof_substring_before, $var5_resultof_cast, xs:double(xs:decimal('8')))"/>
				<xsl:variable name="var10_resultof_substring" as="xs:string" select="fn:substring($var7_resultof_substring_before, xs:double(xs:decimal('9')), xs:double(xs:decimal('6')))"/>
				<xsl:attribute name="value" select="fn:concat(fn:concat(fn:concat(fn:concat(fn:concat(fn:concat(fn:concat(fn:concat(fn:concat(fn:concat(fn:substring($var9_resultof_substring, $var5_resultof_cast, xs:double(xs:decimal('4'))), '-'), fn:substring($var9_resultof_substring, $var4_resultof_cast, $var3_resultof_cast)), '-'), fn:substring($var9_resultof_substring, xs:double(xs:decimal('7')), $var3_resultof_cast)), 'T'), fn:concat(fn:concat(fn:concat(fn:concat(fn:substring($var10_resultof_substring, $var5_resultof_cast, $var3_resultof_cast), ':'), fn:substring($var10_resultof_substring, $var2_resultof_cast, $var3_resultof_cast)), ':'), fn:substring($var10_resultof_substring, $var4_resultof_cast, $var3_resultof_cast))), '-'), fn:substring($var8_resultof_substring_after, $var5_resultof_cast, $var3_resultof_cast)), ':'), fn:substring($var8_resultof_substring_after, $var2_resultof_cast, $var3_resultof_cast))"/>
			</xsl:for-each>
		</xsl:variable>
		<DiagnosticReport xmlns="http://hl7.org/fhir" xmlns:xhtml="http://www.w3.org/1999/xhtml">
			<xsl:attribute name="xsi:schemaLocation" namespace="http://www.w3.org/2001/XMLSchema-instance" select="'http://hl7.org/fhir C:/Users/JERRYG~1/Dropbox/Cognitive/MAPFOR~1/fhir-all-xsd_12/diagnosticreport.xsd'"/>
			<contained>
				<Patient>
					<xsl:attribute name="id" namespace="" select="'pateint'"/>
					<xsl:for-each select="($var1_HLMessage/*:PID[fn:namespace-uri() eq '']/*:PID.3[fn:namespace-uri() eq ''])[(fn:string-length(fn:string(*:PID.3.1[fn:namespace-uri() eq ''])) &gt; xs:decimal('0'))]">
						<xsl:variable name="var12_resultof_cast" as="xs:string" select="fn:string(*:PID.3.1[fn:namespace-uri() eq ''])"/>
						<xsl:variable name="var13_resultof_greater" as="xs:boolean" select="(fn:string-length($var12_resultof_cast) &gt; xs:decimal('0'))"/>
						<identifier>
							<system>
								<xsl:if test="$var13_resultof_greater">
									<xsl:attribute name="value" namespace="" select="xs:string(xs:anyURI(fn:string(*:PID.3.5[fn:namespace-uri() eq ''])))"/>
								</xsl:if>
							</system>
							<value>
								<xsl:if test="$var13_resultof_greater">
									<xsl:attribute name="value" namespace="" select="$var12_resultof_cast"/>
								</xsl:if>
							</value>
						</identifier>
					</xsl:for-each>
					<xsl:for-each select="($var1_HLMessage/*:PID[fn:namespace-uri() eq '']/*:PID.5[fn:namespace-uri() eq ''])[('L' = fn:string(*:PID.5.7[fn:namespace-uri() eq '']))]">
						<name>
							<family>
								<xsl:attribute name="value" namespace="" select="fn:string(*:PID.5.1[fn:namespace-uri() eq ''])"/>
							</family>
							<given>
								<xsl:attribute name="value" namespace="" select="fn:string(*:PID.5.2[fn:namespace-uri() eq ''])"/>
							</given>
						</name>
					</xsl:for-each>
					<gender>
						<coding>
							<code>
								<xsl:for-each select="$var1_HLMessage">
									<xsl:attribute name="value" namespace="" select="fn:string(*:PID[fn:namespace-uri() eq '']/*:PID.8[fn:namespace-uri() eq '']/*:PID.8.1[fn:namespace-uri() eq ''])"/>
								</xsl:for-each>
							</code>
						</coding>
					</gender>
					<birthDate>
						<xsl:for-each select="$var1_HLMessage">
							<xsl:variable name="var14_resultof_cast" as="xs:double" select="xs:double(xs:decimal('2'))"/>
							<xsl:variable name="var15_resultof_cast" as="xs:string" select="xs:string(xs:integer(fn:string(*:PID[fn:namespace-uri() eq '']/*:PID.7[fn:namespace-uri() eq '']/*:PID.7.1[fn:namespace-uri() eq ''])))"/>
							<xsl:attribute name="value" namespace="" select="fn:concat(fn:concat(fn:concat(fn:concat(fn:substring($var15_resultof_cast, xs:double(xs:decimal('1')), xs:double(xs:decimal('4'))), '-'), fn:substring($var15_resultof_cast, xs:double(xs:decimal('5')), $var14_resultof_cast)), '-'), fn:substring($var15_resultof_cast, xs:double(xs:decimal('7')), $var14_resultof_cast))"/>
						</xsl:for-each>
					</birthDate>
				</Patient>
			</contained>
			<xsl:for-each select="$var1_HLMessage">
				<xsl:variable name="var16_resultof_cast" as="xs:double" select="xs:double(xs:decimal('3'))"/>
				<xsl:variable name="var17_resultof_cast" as="xs:double" select="xs:double(xs:decimal('2'))"/>
				<xsl:variable name="var18_resultof_cast" as="xs:double" select="xs:double(xs:decimal('5'))"/>
				<xsl:variable name="var19_resultof_cast" as="xs:double" select="xs:double(xs:decimal('1'))"/>
				<xsl:variable name="var20_resultof_first" as="node()" select="*:OBX[fn:namespace-uri() eq '']"/>
				<xsl:variable name="var21_resultof_first" as="node()" select="$var20_resultof_first/*:OBX.3[fn:namespace-uri() eq '']"/>
				<xsl:variable name="var22_resultof_cast" as="xs:string" select="fn:string($var21_resultof_first/*:OBX.3.2[fn:namespace-uri() eq ''])"/>
				<xsl:variable name="var23_resultof_cast" as="xs:string" select="fn:string($var20_resultof_first/*:OBX.6[fn:namespace-uri() eq '']/*:OBX.6.1[fn:namespace-uri() eq ''])"/>
				<xsl:variable name="var24_resultof_cast" as="xs:string" select="fn:string($var20_resultof_first/*:OBX.14[fn:namespace-uri() eq '']/*:OBX.14.1[fn:namespace-uri() eq ''])"/>
				<xsl:variable name="var25_resultof_cast" as="xs:string" select="fn:string($var20_resultof_first/*:OBX.7[fn:namespace-uri() eq '']/*:OBX.7.1[fn:namespace-uri() eq ''])"/>
				<xsl:variable name="var26_resultof_substring_after" as="xs:string" select="fn:substring-after($var24_resultof_cast, '-')"/>
				<xsl:variable name="var27_resultof_substring_before" as="xs:string" select="fn:substring-before($var24_resultof_cast, '-')"/>
				<xsl:variable name="var28_resultof_substring" as="xs:string" select="fn:substring($var27_resultof_substring_before, xs:double(xs:decimal('9')), xs:double(xs:decimal('6')))"/>
				<xsl:variable name="var29_resultof_substring" as="xs:string" select="fn:substring($var27_resultof_substring_before, $var19_resultof_cast, xs:double(xs:decimal('8')))"/>
				<contained>
					<Observation>
						<xsl:attribute name="id" namespace="" select="fn:concat('observation', xs:string(xs:decimal('1')))"/>
						<name>
							<coding>
								<system>
									<xsl:attribute name="value" namespace="" select="xs:string(xs:anyURI(fn:string($var21_resultof_first/*:OBX.3.3[fn:namespace-uri() eq ''])))"/>
								</system>
								<code>
									<xsl:attribute name="value" namespace="" select="fn:string($var21_resultof_first/*:OBX.3.1[fn:namespace-uri() eq ''])"/>
								</code>
								<display>
									<xsl:attribute name="value" namespace="" select="$var22_resultof_cast"/>
								</display>
							</coding>
							<text>
								<xsl:attribute name="value" namespace="" select="$var22_resultof_cast"/>
							</text>
						</name>
						<valueString>
							<xsl:attribute name="value" namespace="" select="xs:string(xs:decimal(fn:string($var20_resultof_first/*:OBX.5[fn:namespace-uri() eq '']/*:OBX.5.1[fn:namespace-uri() eq ''])))"/>
						</valueString>
						<appliesDateTime>
							<xsl:attribute name="value" namespace="" select="fn:concat(fn:concat(fn:concat(fn:concat(fn:concat(fn:concat(fn:concat(fn:concat(fn:concat(fn:concat(fn:substring($var29_resultof_substring, $var19_resultof_cast, xs:double(xs:decimal('4'))), '-'), fn:substring($var29_resultof_substring, $var18_resultof_cast, $var17_resultof_cast)), '-'), fn:substring($var29_resultof_substring, xs:double(xs:decimal('7')), $var17_resultof_cast)), 'T'), fn:concat(fn:concat(fn:concat(fn:concat(fn:substring($var28_resultof_substring, $var19_resultof_cast, $var17_resultof_cast), ':'), fn:substring($var28_resultof_substring, $var16_resultof_cast, $var17_resultof_cast)), ':'), fn:substring($var28_resultof_substring, $var18_resultof_cast, $var17_resultof_cast))), '-'), fn:substring($var26_resultof_substring_after, $var19_resultof_cast, $var17_resultof_cast)), ':'), fn:substring($var26_resultof_substring_after, $var16_resultof_cast, $var17_resultof_cast))"/>
						</appliesDateTime>
						<status>
							<xsl:attribute name="value" namespace="">
								<xsl:call-template name="vmf:vmf1_inputtoresult">
									<xsl:with-param name="input" select="fn:string($var20_resultof_first/*:OBX.11[fn:namespace-uri() eq '']/*:OBX.11.1[fn:namespace-uri() eq ''])" as="xs:string"/>
								</xsl:call-template>
							</xsl:attribute>
						</status>
						<reliability>
							<xsl:attribute name="value" namespace="" select="'ok'"/>
						</reliability>
						<subject>
							<reference>
								<xsl:attribute name="value" namespace="" select="'#patient'"/>
							</reference>
						</subject>
						<referenceRange>
							<low>
								<value>
									<xsl:attribute name="value" namespace="" select="xs:string(xs:decimal(fn:substring-before($var25_resultof_cast, '-')))"/>
								</value>
								<units>
									<xsl:attribute name="value" namespace="" select="$var23_resultof_cast"/>
								</units>
							</low>
							<high>
								<value>
									<xsl:attribute name="value" namespace="" select="xs:string(xs:decimal(fn:substring-after($var25_resultof_cast, '-')))"/>
								</value>
								<units>
									<xsl:attribute name="value" namespace="" select="$var23_resultof_cast"/>
								</units>
							</high>
						</referenceRange>
					</Observation>
				</contained>
			</xsl:for-each>
			<contained>
				<Practitioner>
					<xsl:attribute name="id" namespace="" select="'orderingprovider1'"/>
					<identifier>
						<system>
							<xsl:for-each select="$var1_HLMessage">
								<xsl:attribute name="value" namespace="" select="xs:string(xs:anyURI(fn:substring-after(fn:string(*:ORC[fn:namespace-uri() eq '']/*:ORC.12[fn:namespace-uri() eq '']/*:ORC.12.1[fn:namespace-uri() eq '']), '-')))"/>
							</xsl:for-each>
						</system>
						<value>
							<xsl:for-each select="$var1_HLMessage">
								<xsl:attribute name="value" namespace="" select="fn:substring-before(fn:string(*:ORC[fn:namespace-uri() eq '']/*:ORC.12[fn:namespace-uri() eq '']/*:ORC.12.1[fn:namespace-uri() eq '']), '-')"/>
							</xsl:for-each>
						</value>
					</identifier>
					<name>
						<family>
							<xsl:for-each select="$var1_HLMessage">
								<xsl:attribute name="value" namespace="" select="fn:string(*:ORC[fn:namespace-uri() eq '']/*:ORC.12[fn:namespace-uri() eq '']/*:ORC.12.2[fn:namespace-uri() eq ''])"/>
							</xsl:for-each>
						</family>
						<given>
							<xsl:for-each select="$var1_HLMessage">
								<xsl:attribute name="value" namespace="" select="fn:string(*:ORC[fn:namespace-uri() eq '']/*:ORC.12[fn:namespace-uri() eq '']/*:ORC.12.3[fn:namespace-uri() eq ''])"/>
							</xsl:for-each>
						</given>
					</name>
				</Practitioner>
			</contained>
			<name>
				<coding>
					<system>
						<xsl:for-each select="$var1_HLMessage">
							<xsl:attribute name="value" namespace="" select="xs:string(xs:anyURI(fn:string(*:OBR[fn:namespace-uri() eq '']/*:OBR.4[fn:namespace-uri() eq '']/*:OBR.4.3[fn:namespace-uri() eq ''])))"/>
						</xsl:for-each>
					</system>
					<code>
						<xsl:for-each select="$var1_HLMessage">
							<xsl:attribute name="value" namespace="" select="xs:string(xs:decimal(fn:string(*:OBR[fn:namespace-uri() eq '']/*:OBR.4[fn:namespace-uri() eq '']/*:OBR.4.1[fn:namespace-uri() eq ''])))"/>
						</xsl:for-each>
					</code>
				</coding>
				<text>
					<xsl:for-each select="$var1_HLMessage">
						<xsl:attribute name="value" namespace="" select="fn:string(*:OBR[fn:namespace-uri() eq '']/*:OBR.4[fn:namespace-uri() eq '']/*:OBR.4.2[fn:namespace-uri() eq ''])"/>
					</xsl:for-each>
				</text>
			</name>
			<status>
				<xsl:attribute name="value" namespace="" select="'final'"/>
			</status>
			<issued>
				<xsl:sequence select="$var11_resultof_map"/>
			</issued>
			<subject>
				<reference>
					<xsl:attribute name="value" namespace="" select="'#patient'"/>
				</reference>
			</subject>
			<performer>
				<display>
					<xsl:attribute name="value" namespace="" select="'vista'"/>
				</display>
			</performer>
			<diagnosticPeriod>
				<start>
					<xsl:sequence select="$var11_resultof_map"/>
				</start>
			</diagnosticPeriod>
			<xsl:for-each select="$var1_HLMessage">
				<result>
					<reference>
						<xsl:attribute name="value" namespace="" select="fn:concat('#observation', xs:string(xs:decimal('1')))"/>
					</reference>
				</result>
			</xsl:for-each>
		</DiagnosticReport>
	</xsl:template>
</xsl:stylesheet>
