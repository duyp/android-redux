<?xml version="1.0"?>
<recipe>

    <instantiate from="src/app_package/StateMachine.kt.ftl"
                   to="${escapeXmlAttribute(srcOut)}/${featureName}StateMachine.kt" />

    <instantiate from="src/app_package/ViewModel.kt.ftl"
                   to="${escapeXmlAttribute(srcOut)}/${featureName}ViewModel.kt" />

<#if useFragment>
    <instantiate from="src/app_package/Fragment.kt.ftl"
                   to="${escapeXmlAttribute(srcOut)}/${featureName}Fragment.kt" />

    <instantiate from="src/app_package/Module_Fragment.kt.ftl"
                   to="${escapeXmlAttribute(srcOut)}/${featureName}Module.kt" />
<#else>
    <instantiate from="src/app_package/Activity.kt.ftl"
                   to="${escapeXmlAttribute(srcOut)}/${featureName}Activity.kt" />

    <instantiate from="src/app_package/Module_Activity.kt.ftl"
                   to="${escapeXmlAttribute(srcOut)}/${featureName}ActivityModule.kt" />

   <merge from="AndroidManifest.xml.ftl"
                to="${escapeXmlAttribute(manifestOut)}/AndroidManifest.xml" />
</#if>

<#if hasTracking>
    <instantiate from="src/app_package/Tracking.kt.ftl"
                   to="${escapeXmlAttribute(srcOut)}/${featureName}Tracking.kt" />
</#if>

    <open file="${srcOut}/${featureName}StateMachine.kt"/>
    <open file="${srcOut}/${featureName}ViewModel.kt"/>
    <#if !useFragment>
        <open file="${srcOut}/${featureName}Activity.kt"/>
    </#if>

</recipe>
