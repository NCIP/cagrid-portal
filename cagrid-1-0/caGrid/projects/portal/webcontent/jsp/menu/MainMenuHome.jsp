<%-- tabs begins --%>
<table summary="" cellpadding="0" cellspacing="0" border="0" width="100%">
    <tr>
        <td width="1"><%-- anchor to skip main menu --%><a href="#content"><img src="images/shim.gif" alt="Skip Menu"
                                                                                width="1" height="1" border="0"/></a>
        </td>

        <td class="mainMenuSpacer">&nbsp;</td>
        <td height="20" class="mainMenuItemOn" onmouseover="changeMenuStyle(this,'mainMenuItemOn'),showCursor()"
            onmouseout="changeMenuStyle(this,'mainMenuItemOn'),hideCursor()"
            onclick="document.location.href='home.jsf'">
            <a class="mainMenuLink" href="home.jsf">HOME</a>
        </td>
        <td class="mainMenuSpacer">&nbsp;</td>
        <td height="20" class="mainMenuItem" onmouseover="changeMenuStyle(this,'mainMenuItemOver'),showCursor()"
            onmouseout="changeMenuStyle(this,'mainMenuItem'),hideCursor()"
            onclick="document.location.href='directory.jsf'">
            <a class="mainMenuLink" href="directory.jsf">DIRECTORY</a>
        </td>
        <td class="mainMenuSpacer">&nbsp;</td>
        <td height="20" class="mainMenuItem" onmouseover="changeMenuStyle(this,'mainMenuItemOver'),showCursor()"
            onmouseout="changeMenuStyle(this,'mainMenuItem'),hideCursor()" onclick="document.location.href='map.jsf'">
            <a class="mainMenuLink" href="map.jsf">MAP VIEW</a>
        </td>
        <td class="mainMenuSpacer">&nbsp;</td>
        <td height="20" class="mainMenuItem" onmouseover="changeMenuStyle(this,'mainMenuItemOver'),showCursor()"
            onmouseout="changeMenuStyle(this,'mainMenuItem'),hideCursor()" onclick="document.location.href='help.jsf'">
            <a class="mainMenuLink" href="help.jsf">HELP</a>
        </td>

        <td width="99%" class="mainMenuSpacer">&nbsp;</td>
    </tr>
</table>
<%-- tabs ends --%>

