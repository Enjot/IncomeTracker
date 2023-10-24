package ui.leftcontent

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Done
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sqldelight.Category
import com.example.sqldelight.Spending
import ui.CurrentScreen
import ui.HomeScreenModel
import ui.rightcontent.shape

@Composable
fun ListOfSpending(
    screenModel: HomeScreenModel,
    onAddClick: (String, Double, Category) -> Unit,
    onDeleteClick: (Long) -> Unit,
    selectedCategory: Category?,
    onChooseCategoryButtonClick: () -> Unit,
    onSelectCategory: (String) -> Unit,
    spendings: List<Spending>,
    categories: List<Category>,
    modifier: Modifier = Modifier
) {
    
    Card(
        backgroundColor = MaterialTheme.colors.surface,
        elevation = 4.dp,
        shape = shape,
        modifier = modifier
    ) {

        // use box to stack elements, in our case: list, scrollbar and floating action button

        when (screenModel.currentScreen.value) {
            is CurrentScreen.ListOfSpending -> {
                Spendings(
                    onAddClick = { screenModel.currentScreen.value = CurrentScreen.AddEditSpending },
                    list = spendings,
                    onDeleteClick = onDeleteClick,
                    modifier = modifier
                )
            }

            is CurrentScreen.AddEditSpending -> {
                AddEditSpending(
                    onBackClick = { screenModel.currentScreen.value = CurrentScreen.ListOfSpending },
                    onAddClick = onAddClick,
                    onChooseCategoryButtonClick = onChooseCategoryButtonClick,
                    selectedCategory = selectedCategory,
                    modifier = modifier
                )
            }

            is CurrentScreen.ChooseCategory -> {
                Categories(
                    categories,
                    onSelectCategory,
                )
            }

        }
    }
}

@Composable
fun Spendings(
    list: List<Spending>,
    onAddClick: () -> Unit = {},
    onClick: (Spending) -> Unit = {},
    onDeleteClick: (Long) -> Unit,
    modifier: Modifier
) {

    val stateVertical = rememberLazyListState()

    Box(modifier) {
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 12.dp),
            userScrollEnabled = true,
            state = stateVertical,
            modifier = modifier
                .padding(
                    end = if (stateVertical.canScrollForward || stateVertical.canScrollBackward) 8.dp else 0.dp
                )
                .align(Alignment.CenterStart)

        ) {
            item {
                // use list of items and show every element
                list.forEach {
                    SingleSpendingCard(
                        item = it,
                        onClick = onDeleteClick,
                        modifier = Modifier
                            .padding(4.dp)
                    )
                }
                // add spacer at the end of list so floating action button doesn't cover last item
                /* TODO make it like spacer height depends on button height */
                Spacer(modifier = Modifier.height(56.dp))
            }
        }
        // rest of elements in box (scrollbar and floating action button)
        VerticalScrollbar(
            adapter = rememberScrollbarAdapter(stateVertical),
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight()
        )

        val buttonAlignment by animateAlignmentAsState(if (list.isEmpty()) Alignment.Center else Alignment.BottomEnd)

        Button(
            onClick = onAddClick,
            shape = RoundedCornerShape(100),
            modifier = Modifier
                .align(
                    alignment = buttonAlignment
                )
                .padding(end = 24.dp)
        ) {
            // elements inside button
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(horizontal = 12.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Dodaj wydatek")
            }

        }

    }
}

@Composable
fun animateAlignmentAsState(
    targetAlignment: Alignment,
): State<Alignment> {
    val biased = targetAlignment as BiasAlignment
    val horizontal by animateFloatAsState(biased.horizontalBias)
    val vertical by animateFloatAsState(biased.verticalBias)
    return derivedStateOf { BiasAlignment(horizontal, vertical) }
}

@Composable
fun AddEditSpending(
    onAddClick: (String, Double, Category) -> Unit,
    onBackClick: () -> Unit,
    onChooseCategoryButtonClick: () -> Unit,
    selectedCategory: Category?,
    modifier: Modifier
) {

    var spendingName by remember { mutableStateOf("") }
    var spendingAmount by remember { mutableStateOf("") }

    Box(
        modifier = modifier
    ) {
        Button(
            onClick = onBackClick,
            shape = RoundedCornerShape(100),
            modifier = Modifier
                .align(Alignment.TopStart)
        ) {
            Icon(
                imageVector = Icons.Outlined.ArrowBack,
                contentDescription = null
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
        ) {
            OutlinedTextField(
                value = spendingName,
                label = { Text("nazwa") },
                onValueChange = { spendingName = it },
                singleLine = true,
                modifier = Modifier
                    .padding(12.dp)
            )
            OutlinedTextField(
                value = selectedCategory?.name ?: "Wybierz kategorię",
                enabled = false,
                label = {
                    Text("Kategoria")
                },
                onValueChange = { },
                singleLine = true,
                modifier = Modifier
                    .padding(12.dp)
                    .clickable { 
                        run(onChooseCategoryButtonClick)
                    }
            )
            OutlinedTextField(
                value = spendingAmount,
                label = { Text("Kwota") },
                onValueChange = { spendingAmount = it },
                singleLine = true,
                modifier = Modifier
                    .padding(12.dp)
                    .clickable { 
                        
                    }
            )
        }
        
        Button(
            onClick = {
                if (selectedCategory == null) return@Button
                onAddClick(spendingName, spendingAmount.toDouble(), selectedCategory!!)
                run(onBackClick)
            },
            shape = RoundedCornerShape(100),
            modifier = Modifier
                .align(Alignment.BottomEnd)
        ) {
            Icon(
                imageVector = Icons.Outlined.Done,
                contentDescription = null
            )
        }
    }
}

@Composable
fun Categories(
    categories: List<Category>,
    onClick: (String) -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(100.dp),
        modifier = Modifier
            .padding(12.dp)
        ) {
        items(categories) {
            Text(
                text = it.name,
                maxLines = 1,
                modifier = Modifier
                    .clickable { run { onClick(it.name) } }
                    .padding(4.dp)
            )
        }
    }
}


