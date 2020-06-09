// toggle between current version and version 1
function changeVersionDisplayed( buttonId, visibleGraphId ) {
	const graphs = document.getElementsByClassName( "version-graphs" );
	const buttons = document.getElementsByClassName( "version-buttons" );

	for( let i = 0; i < graphs.length; i++ ) {
		// hide all the graph
		graphs[i].style.display = "none";

		// change background color of all button to lightblue
		buttons[i].style.backgroundColor = "#b3d9ff";
	}

	// display only the graph corresponding to the version chosen
	document.getElementById( visibleGraphId ).style.display = "table-cell";

	// change the background color of the version button clicked
	document.getElementById( buttonId ).style.backgroundColor = "white";
}

// close the version popup
function hideVersion() {
	document.getElementById( "version-popup" ).style.display = "none";
}

// replace the current file with the version chosen
function changeFileVersion() {
	// close the version popup
	hideVersion();

	// show a file successfully replaced alert
	fileSuccessfullySharedAlert( "File version successfully updated!" );
}