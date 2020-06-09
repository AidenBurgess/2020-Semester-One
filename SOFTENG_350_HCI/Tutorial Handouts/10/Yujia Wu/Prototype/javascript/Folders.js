// triggered when the user clicks on one folder
function hoverFolderImage( folderButtonId ) {
	// get the image of the folder clicked
	const img = document.getElementById(folderButtonId).getElementsByClassName("folder")[0];

	// decrease the opacity of the folder to show the background color of the button that wrapped up this image
	// this will look like the image is hovered by a grey rectangle
	img.style.opacity = "0.5";
}

// triggered when the user clicks something other than the folder he clicked previously
function resetImage( folderButtonId ) {
	// get the image of the folder clicked previously
	const img = document.getElementById(folderButtonId).getElementsByClassName("folder")[0];

	// change the opacity of the image to 1, users
	img.style.opacity = "1";
}

// triggered when the user double clicks on a folder
// folderButtonId: the id of the folder clicked
function openFolder( folderButtonId ) {
	// hide top folder directory
	const dirs = document.getElementsByClassName( "file-path" )
	for( let i = 0; i < dirs.length; i++ ) {
		dirs[i].style.display = "none";
	}

	//display the corresponding path button
	const substr = folderButtonId.substr(0,folderButtonId.indexOf("-"));
	btn = document.getElementById("folderNameButton")
	btn.innerHTML = substr + '<i class="fa fa-caret-down"></i>';
	document.getElementById("softeng350-folder-path").style.display = "block";

	// hide quick access
	document.getElementById( "quick-access-component" ).style.display = "none";

	// change folders
	document.getElementById( "route-folder-container" ).style.display = "none";
	document.getElementById( "softeng350-folder-container" ).style.display = "flex";

	// hide files
	document.getElementById( "file-section" ).style.display = "none";

	// only show files if user clicks softeng 350 folder
	if( folderButtonId === "Softeng350-folder" ) {
		//show files
		document.getElementById( "file-section" ).style.display = "block";
		document.getElementById("folders-tittle").style.justifyContent = "between";
	}

}