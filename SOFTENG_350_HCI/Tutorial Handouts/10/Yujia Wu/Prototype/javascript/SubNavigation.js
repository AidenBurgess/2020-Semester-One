// triggered after clicking My Drive button or the logo of the website
function goToRootDirectory() {
	// change directory
	document.getElementById( "softeng350-folder-path" ).style.display = "none";
	document.getElementById( "homepage-dropdown-button" ).style.display = "block";

	// show quick access
	document.getElementById( "quick-access-component" ).style.display = "block";

	// change folder displayed
	document.getElementById( "route-folder-container" ).style.display = "flex";
	document.getElementById( "softeng350-folder-container" ).style.display = "none";

	// hide files
	document.getElementById( "file-section" ).style.display = "none";
}

// triggered when graph icon is clicked
function showFileRelationGraph() {
	// check if users are in the softeng-350 folder
	const files = document.getElementById( "file-section" );

	// if users are not in the softeng 350 folder
	if( files.style.display !== "none" ) {
		// display the popup if users are in softeng350 folder
		document.getElementById( "file-relation-popup" ).style.display = "block";
		showAllGraphs();
	} else {
		// assume users are in root folder
		document.getElementById( "file-relation-popup" ).style.display = "block";
		showEmptyGraph();
	}
}
