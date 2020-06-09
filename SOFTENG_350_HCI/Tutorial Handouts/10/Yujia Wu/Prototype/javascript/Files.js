// triggered when users click on the file
function hoverFileImage( fileButtonId ) {
	// get all the files element
	const files = document.getElementById( fileButtonId ).getElementsByClassName("file");

	// hide the first file element which is the image of the file before clicked
	files[0].style.display = "none";

	// show the second file element which is the image of the file after clicked
	files[1].style.display = "block";
}

// triggered when users click something else that is not the file they clicked before
function resetFileImage( fileButtonId ) {
	// get all the files element
	const files = document.getElementById( fileButtonId ).getElementsByClassName( "file" );

	// show the first file element which is the image of the file before clicked
	files[0].style.display = "block";

	// hide the second file element which is the image of the file after clicked
	files[1].style.display = "none";
}

// users right click on a file
function rightButtonClicked( dropdownContentId ) {
	// hide all dropdownContent first
	document.getElementsByClassName( "dropdown-content" )[0].style.display = "none";
	document.getElementsByClassName( "dropdown-content" )[1].style.display = "none";

	// display the dropdown box beside the file being clicked
	document.getElementById( dropdownContentId ).style.display = "block"
}

// display the popups showing previous version of the file
function showVersionPopup(iconId) {
	// title of the popup
	const tittle = "Previous version for " + iconId;
	document.getElementById("version-title").innerText = tittle;

	// show the popup
	document.getElementById( "version-popup" ).style.display = "block";
}

// Close the dropdown if the user clicks outside of it
window.onclick = function(event) {
	if( !event.target.matches('.dropdown-content') ) {
		// hide both dropdown box
		document.getElementsByClassName( "dropdown-content" )[0].style.display = "none";
		document.getElementsByClassName( "dropdown-content" )[1].style.display = "none";
	}
}
