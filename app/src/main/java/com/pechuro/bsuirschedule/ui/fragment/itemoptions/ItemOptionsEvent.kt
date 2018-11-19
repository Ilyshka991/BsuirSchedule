package com.pechuro.bsuirschedule.ui.fragment.itemoptions

sealed class ItemOptionsEvent

class OnDeleted(val itemId: Int) : ItemOptionsEvent()
