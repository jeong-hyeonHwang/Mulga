package com.ilm.mulga.feature.login

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.ilm.mulga.R
import com.ilm.mulga.feature.login.component.SocialLoginButton
import com.ilm.mulga.ui.theme.MulGaTheme


@Composable
fun GoogleLoginButton(onClick: () -> Unit) {
    val googleIcon = painterResource(id = R.drawable.ic_logo_google)
    val googleText = stringResource(id = R.string.login_google)
    val googleBorderColor = MulGaTheme.colors.google_border
    val googleBackgroundColor = MulGaTheme.colors.white1
    val googleTextColor = MulGaTheme.colors.google_font
    val googleTextStyle = MulGaTheme.typography.google
    SocialLoginButton(
        text = googleText,
        icon = googleIcon,
        onClick = onClick,
        borderColor = googleBorderColor,
        backgroundColor = googleBackgroundColor,
        textColor = googleTextColor,
        textStyle = googleTextStyle
    )
}

@Preview(showBackground = true)
@Composable
fun GoogleLoginButtonPreview() {
    GoogleLoginButton(onClick = {
        println("구글 로그인!")
    })
}
