# Pre-work SimpleTodo

SimpleTodo is an android app that allows building a todo list and basic todo
items management functionality including adding new items, editing and
deleting an existing item.

Submitted by: Heung Tai

Time spent: 3 hours

## User Stories

The following **required** functionality is completed:

* [x] User can **successfully add and remove items** from the todo list
* [x] User can **tap a todo item in the list and bring up an edit screen for
  the todo item** and then have any changes to the text reflected in the todo
  list.
* [x] User can **persist todo items** and retrieve them properly on app restart

The following optional features are implemented:

* [x] Filter the todo items by a search query
* [x] Persist the todo items into SQLite instead of a text file
* [x] Improve style of the todo items in the list using a custom adapter
* [x] Use a DialogFragment to add new items
* [x] Use a DialogFragment instead of new Activity for editing items
* [x] Add support for selecting the priority of each todo item (and display in
  listview item)

# Video Walkthrough

Here's a walkthrough of implemented user stories:

![video walkthrough for the required functionalities
completed](simpletodo.gif)

GIF created with LiceCap.

# Notes

LiceCap has a bug that it does not work well with secondary screen. Whenever I
open it and at some point drag it to a secondary screen, whatever I record
after that will only record a black screen with marks for my cursor.
Re-recording a new one does not work either. The workaround is to re-open
LiceCap again and make sure it does not touch my secondary screen at any point
of its life.

# License

    Copyright 2016 Heung Tai

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
