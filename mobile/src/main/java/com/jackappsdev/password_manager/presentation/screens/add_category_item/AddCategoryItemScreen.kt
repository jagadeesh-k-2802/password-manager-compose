package com.jackappsdev.password_manager.presentation.screens.add_category_item

import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.jackappsdev.password_manager.R
import com.jackappsdev.password_manager.shared.constants.colorList
import com.jackappsdev.password_manager.core.parseColor
import com.jackappsdev.password_manager.presentation.composables.UnsavedChangesDialog
import com.jackappsdev.password_manager.presentation.theme.pagePadding
import com.jackappsdev.password_manager.presentation.theme.windowinsetsVerticalZero
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Used to auto-select category in Add/Edit Password Screens
 */
const val CREATED_CATEGORY = "CREATED_CATEGORY_KEY"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCategoryItemScreen(
    navController: NavController,
    viewModel: AddCategoryItemViewModel = hiltViewModel()
) {
    var name by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current
    var color by rememberSaveable { mutableStateOf(colorList.first()) }
    val error by viewModel.errorChannel.receiveAsFlow().collectAsState(initial = null)
    var isUnsavedChangesDialogVisible by rememberSaveable { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    val lifecycleOwner = LocalLifecycleOwner.current
    val backDispatcher = checkNotNull(LocalOnBackPressedDispatcherOwner.current)
    val dispatcher = backDispatcher.onBackPressedDispatcher
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    val backCallback = remember(name) {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (name.isNotEmpty()) {
                    isUnsavedChangesDialogVisible = true
                } else {
                    navController.navigateUp()
                }
            }
        }
    }

    DisposableEffect(lifecycleOwner, backDispatcher) {
        dispatcher.addCallback(lifecycleOwner, backCallback)
        onDispose { backCallback.remove() }
    }

    if (isUnsavedChangesDialogVisible) UnsavedChangesDialog(
        onConfirm = { navController.navigateUp() },
        onDismiss = { isUnsavedChangesDialogVisible = false }
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.title_add_new_category)) },
                navigationIcon = {
                    IconButton(onClick = { backCallback.handleOnBackPressed() }) {
                        Icon(
                            Icons.AutoMirrored.Rounded.ArrowBack,
                            stringResource(R.string.accessibility_go_back)
                        )
                    }
                },
                windowInsets = windowinsetsVerticalZero
            )
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .padding(horizontal = pagePadding)
                .verticalScroll(scrollState)
        ) {
            OutlinedTextField(
                value = name,
                isError = error is AddCategoryItemError.NameError,
                supportingText = {
                    error?.let {
                        if (it is AddCategoryItemError.NameError) Text(it.error)
                    }
                },
                onValueChange = { value -> name = value },
                label = { Text(stringResource(R.string.label_name)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Done
                )
            )

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(R.string.label_category_color),
                style = MaterialTheme.typography.labelLarge
            )
            Spacer(modifier = Modifier.height(10.dp))

            LazyRow {
                items(colorList) { item ->
                    Box(
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .clip(CircleShape)
                            .background(parseColor(item))
                            .size(64.dp)
                            .clickable { color = item }
                    ) {
                        if (color == item) Icon(
                            imageVector = Icons.Outlined.Done,
                            tint = Color.Black,
                            contentDescription = context.getString(R.string.accessibility_selected_color),
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .fillMaxWidth()
                                .padding(20.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    viewModel.addItem(name, color) { categoryModel ->
                        navController.navigateUp()
                        val savedState = navController.currentBackStackEntry?.savedStateHandle
                        savedState?.set(CREATED_CATEGORY, Json.encodeToString(categoryModel))
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Outlined.Done, stringResource(R.string.accessibility_create))
                Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                Text(stringResource(R.string.btn_create))
            }
        }
    }
}
