package com.jackappsdev.password_manager.shared.base

import kotlinx.coroutines.flow.Flow

/**
 * Interface for ViewModels that can handle events and emit effects
 */
interface EventDrivenViewModel<Event, Effect> {
    val effectFlow: Flow<Effect>
    fun onEvent(event: Event)
}
