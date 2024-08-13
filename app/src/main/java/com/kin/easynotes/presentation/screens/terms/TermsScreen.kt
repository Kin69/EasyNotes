package com.kin.easynotes.presentation.screens.terms

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kin.easynotes.R
import com.kin.easynotes.core.constant.ConnectionConst
import com.kin.easynotes.core.constant.SupportConst
import com.kin.easynotes.presentation.components.AgreeButton
import com.kin.easynotes.presentation.components.NotesScaffold
import com.kin.easynotes.presentation.components.markdown.MarkdownText
import com.kin.easynotes.presentation.screens.settings.model.SettingsViewModel
import com.kin.easynotes.presentation.screens.settings.settings.shapeManager


@Composable
fun TermsScreen(
    settingsViewModel: SettingsViewModel
) {
    NotesScaffold(
        floatingActionButton = {
            AgreeButton(
                text = stringResource(id = R.string.agree),
                modifier = Modifier.semantics {
                    contentDescription = "Agree"
                }
            ) {
                settingsViewModel.update(settingsViewModel.settings.value.copy(termsOfService = true))
            }
        },
        content = {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.terms_of_service),
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.padding(0.dp,16.dp,16.dp,16.dp)
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(
                            shapeManager(
                                isBoth = true,
                                radius = settingsViewModel.settings.value.cornerRadius
                            )
                        )
                        .background(
                            color = MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.5f)
                        )
                        .padding(1.dp)
                        .semantics {
                            contentDescription = "Terms"
                        }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        MarkdownText(
                            fontSize = 12.sp,
                            radius = settingsViewModel.settings.value.cornerRadius,
                            markdown = getTermsOfService(),
                            isEnabled = true
                        )
                    }
                }
            }
        }
    )
}


@Composable
fun getTermsOfService(): String {
    return buildString {
        append("### ${stringResource(R.string.terms_acceptance_title)}\n")
        append("${stringResource(R.string.terms_acceptance_body)}\n\n")

        append("### ${stringResource(R.string.terms_license_title)}\n")
        append("${stringResource(R.string.terms_license_body)}\n\n")

        append("### ${stringResource(R.string.terms_use_title)}\n")
        append("${stringResource(R.string.terms_use_body)}\n\n")

        append("### ${stringResource(R.string.terms_intellectual_property_title)}\n")
        append("${stringResource(R.string.terms_intellectual_property_body)}\n\n")

        append("### ${stringResource(R.string.terms_disclaimer_title)}\n")
        append("${stringResource(R.string.terms_disclaimer_body)}\n\n")

        append("### ${stringResource(R.string.terms_liability_title)}\n")
        append("${stringResource(R.string.terms_liability_body)}\n\n")

        append("### ${stringResource(R.string.terms_termination_title)}\n")
        append("${stringResource(R.string.terms_termination_body)}\n\n")

        append("### ${stringResource(R.string.terms_changes_title)}\n")
        append("${stringResource(R.string.terms_changes_body)}\n\n")

        append("### ${stringResource(R.string.terms_contact_title)}\n")
        append("${stringResource(R.string.terms_contact_body)} ${ConnectionConst.SUPPORT_MAIL}.\n\n")

        append("### ${stringResource(R.string.terms_privacy_title)}\n")
        append("${stringResource(R.string.terms_privacy_body)} **https://github.com/Kin69/EasyNotes/wiki/Privacy-Policy**.\n\n")

        append("*${stringResource(R.string.terms_effective_date)}: ${SupportConst.TERMS_EFFECTIVE_DATE}\n")
    }
}