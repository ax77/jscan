/\
\
/

int a/**/=1;
int b/**/=1;
int c=a/**//b;

char s0[]="\t\\t\"a\"+\"b\"=\"c\"\\;\\\n\n";

// 
//      char *str = "a\"/*b\"\\\\";
//      char c = '"', d = '\'';
//      int foo (int a, int b) {
//       int f = a /b;
//       int g = a/ b;
//      }
//      int main( int argc, char **argv ) {
//       char str0[]="/*  ML in string */";
//       char str1[]="" "0" "1" "2" "3" "4" "5" "6" "7" "8" "9" ;
//       char str3[]="//";
//       char str4[]="/**/";
//       char str5[]="\n\\\n\\\n\\";
//       return (0);
//      }
// 


//#include <stdio.h>

/*
void test_path_win32__utf8_to_utf16(void)
{

	test_utf8_to_utf16("C:\\", L"\\\\?\\C:\\");
	test_utf8_to_utf16("c:\\", L"\\\\?\\c:\\");
	test_utf8_to_utf16("C:/", L"\\\\?\\C:\\");
	test_utf8_to_utf16("c:/", L"\\\\?\\c:\\");

}

void test_path_win32__removes_trailing_slash(void)
{

	test_utf8_to_utf16("C:\\Foo\\", L"\\\\?\\C:\\Foo");
	test_utf8_to_utf16("C:\\Foo\\\\", L"\\\\?\\C:\\Foo");
	test_utf8_to_utf16("C:\\Foo\\\\", L"\\\\?\\C:\\Foo");
	test_utf8_to_utf16("C:/Foo/", L"\\\\?\\C:\\Foo");
	test_utf8_to_utf16("C:/Foo///", L"\\\\?\\C:\\Foo");

}

void test_path_win32__squashes_multiple_slashes(void)
{

	test_utf8_to_utf16("C:\\\\Foo\\Bar\\\\Foobar", L"\\\\?\\C:\\Foo\\Bar\\Foobar");
	test_utf8_to_utf16("C://Foo/Bar///Foobar", L"\\\\?\\C:\\Foo\\Bar\\Foobar");

}

void test_path_win32__unc(void)
{

	test_utf8_to_utf16("\\\\server\\c$\\unc\\path", L"\\\\?\\UNC\\server\\c$\\unc\\path");
	test_utf8_to_utf16("//server/git/style/unc/path", L"\\\\?\\UNC\\server\\git\\style\\unc\\path");

}

void test_path_win32__honors_max_path(void)
{

	git_win32_path path_utf16;

	test_utf8_to_utf16("C:\\This path is 259 chars and is the max length in windows\\0123456789abcdefghij0123456789abcdefghij0123456789abcdefghij0123456789abcdefghij0123456789abcdefghij0123456789abcdefghij0123456789abcdefghij0123456789abcdefghij0123456789abcdefghij0123456789abcdefghij",
		L"\\\\?\\C:\\This path is 259 chars and is the max length in windows\\0123456789abcdefghij0123456789abcdefghij0123456789abcdefghij0123456789abcdefghij0123456789abcdefghij0123456789abcdefghij0123456789abcdefghij0123456789abcdefghij0123456789abcdefghij0123456789abcdefghij");
	test_utf8_to_utf16("\\\\unc\\paths may also be 259 characters including the server\\123456789abcdefghij0123456789abcdefghij0123456789abcdefghij0123456789abcdefghij0123456789abcdefghij0123456789abcdefghij0123456789abcdefghij0123456789abcdefghij0123456789abcdefghij0123456789abcdefghij",
		L"\\\\?\\UNC\\unc\\paths may also be 259 characters including the server\\123456789abcdefghij0123456789abcdefghij0123456789abcdefghij0123456789abcdefghij0123456789abcdefghij0123456789abcdefghij0123456789abcdefghij0123456789abcdefghij0123456789abcdefghij0123456789abcdefghij");

	cl_check_fail(git_win32_path_from_utf8(path_utf16, "C:\\This path is 260 chars and is sadly too long for windows\\0123456789abcdefghij0123456789abcdefghij0123456789abcdefghij0123456789abcdefghij0123456789abcdefghij0123456789abcdefghij0123456789abcdefghij0123456789abcdefghij0123456789abcdefghij0123456789abcdefghij"));
	cl_check_fail(git_win32_path_from_utf8(path_utf16, "\\\\unc\\paths are also bound by 260 character restrictions\\including the server name portion\\bcdefghij0123456789abcdefghij0123456789abcdefghij0123456789abcdefghij0123456789abcdefghij0123456789abcdefghij0123456789abcdefghij0123456789abcdefghij0123456789abcdefghij"));

}

void test_path_win32__dot_and_dotdot(void)
{

	test_utf8_to_utf16("C:\\Foo\\..\\Foobar", L"\\\\?\\C:\\Foobar");
	test_utf8_to_utf16("C:\\Foo\\Bar\\..\\Foobar", L"\\\\?\\C:\\Foo\\Foobar");
	test_utf8_to_utf16("C:\\Foo\\Bar\\..\\Foobar\\..", L"\\\\?\\C:\\Foo");
	test_utf8_to_utf16("C:\\Foobar\\..", L"\\\\?\\C:\\");
	test_utf8_to_utf16("C:/Foo/Bar/../Foobar", L"\\\\?\\C:\\Foo\\Foobar");
	test_utf8_to_utf16("C:/Foo/Bar/../Foobar/../Asdf/", L"\\\\?\\C:\\Foo\\Asdf");
	test_utf8_to_utf16("C:/Foo/Bar/../Foobar/..", L"\\\\?\\C:\\Foo");
	test_utf8_to_utf16("C:/Foo/..", L"\\\\?\\C:\\");

	test_utf8_to_utf16("C:\\Foo\\Bar\\.\\Foobar", L"\\\\?\\C:\\Foo\\Bar\\Foobar");
	test_utf8_to_utf16("C:\\.\\Foo\\.\\Bar\\.\\Foobar\\.\\", L"\\\\?\\C:\\Foo\\Bar\\Foobar");
	test_utf8_to_utf16("C:/Foo/Bar/./Foobar", L"\\\\?\\C:\\Foo\\Bar\\Foobar");
	test_utf8_to_utf16("C:/Foo/../Bar/./Foobar/../", L"\\\\?\\C:\\Bar");

	test_utf8_to_utf16("C:\\Foo\\..\\..\\Bar", L"\\\\?\\C:\\Bar");

}

void test_path_win32__absolute_from_no_drive_letter(void)
{

	test_utf8_to_utf16("\\Foo", L"\\\\?\\C:\\Foo");
	test_utf8_to_utf16("\\Foo\\Bar", L"\\\\?\\C:\\Foo\\Bar");
	test_utf8_to_utf16("/Foo/Bar", L"\\\\?\\C:\\Foo\\Bar");

}

void test_path_win32__absolute_from_relative(void)
{

	char cwd_backup[MAX_PATH];

	cl_must_pass(p_getcwd(cwd_backup, MAX_PATH));
	cl_must_pass(p_chdir("C:/"));

	test_utf8_to_utf16("Foo", L"\\\\?\\C:\\Foo");
	test_utf8_to_utf16("..\\..\\Foo", L"\\\\?\\C:\\Foo");
	test_utf8_to_utf16("Foo\\..", L"\\\\?\\C:\\");
	test_utf8_to_utf16("Foo\\..\\..", L"\\\\?\\C:\\");
	test_utf8_to_utf16("", L"\\\\?\\C:\\");

	cl_must_pass(p_chdir("C:/Windows"));

	test_utf8_to_utf16("Foo", L"\\\\?\\C:\\Windows\\Foo");
	test_utf8_to_utf16("Foo\\Bar", L"\\\\?\\C:\\Windows\\Foo\\Bar");
	test_utf8_to_utf16("..\\Foo", L"\\\\?\\C:\\Foo");
	test_utf8_to_utf16("Foo\\..\\Bar", L"\\\\?\\C:\\Windows\\Bar");
	test_utf8_to_utf16("", L"\\\\?\\C:\\Windows");

	cl_must_pass(p_chdir(cwd_backup));

}

void test_path_win32__canonicalize(void)
{

	test_canonicalize(L"C:\\Foo\\Bar", L"C:\\Foo\\Bar");
	test_canonicalize(L"C:\\Foo\\", L"C:\\Foo");
	test_canonicalize(L"C:\\Foo\\\\", L"C:\\Foo");
	test_canonicalize(L"C:\\Foo\\..\\Bar", L"C:\\Bar");
	test_canonicalize(L"C:\\Foo\\..\\..\\Bar", L"C:\\Bar");
	test_canonicalize(L"C:\\Foo\\..\\..\\..\\..\\", L"C:\\");
	test_canonicalize(L"C:/Foo/Bar", L"C:\\Foo\\Bar");
	test_canonicalize(L"C:/", L"C:\\");

	test_canonicalize(L"Foo\\\\Bar\\\\Asdf\\\\", L"Foo\\Bar\\Asdf");
	test_canonicalize(L"Foo\\\\Bar\\\\..\\\\Asdf\\", L"Foo\\Asdf");
	test_canonicalize(L"Foo\\\\Bar\\\\.\\\\Asdf\\", L"Foo\\Bar\\Asdf");
	test_canonicalize(L"Foo\\\\..\\Bar\\\\.\\\\Asdf\\", L"Bar\\Asdf");
	test_canonicalize(L"\\", L"");
	test_canonicalize(L"", L"");
	test_canonicalize(L"Foo\\..\\..\\..\\..", L"");
	test_canonicalize(L"..\\..\\..\\..", L"");
	test_canonicalize(L"\\..\\..\\..\\..", L"");

	test_canonicalize(L"\\\\?\\C:\\Foo\\Bar", L"\\\\?\\C:\\Foo\\Bar");
	test_canonicalize(L"\\\\?\\C:\\Foo\\Bar\\", L"\\\\?\\C:\\Foo\\Bar");
	test_canonicalize(L"\\\\?\\C:\\\\Foo\\.\\Bar\\\\..\\", L"\\\\?\\C:\\Foo");
	test_canonicalize(L"\\\\?\\C:\\\\", L"\\\\?\\C:\\");
	test_canonicalize(L"//?/C:/", L"\\\\?\\C:\\");
	test_canonicalize(L"//?/C:/../../Foo/", L"\\\\?\\C:\\Foo");
	test_canonicalize(L"//?/C:/Foo/../../", L"\\\\?\\C:\\");

	test_canonicalize(L"\\\\?\\UNC\\server\\C$\\folder", L"\\\\?\\UNC\\server\\C$\\folder");
	test_canonicalize(L"\\\\?\\UNC\\server\\C$\\folder\\", L"\\\\?\\UNC\\server\\C$\\folder");
	test_canonicalize(L"\\\\?\\UNC\\server\\C$\\folder\\", L"\\\\?\\UNC\\server\\C$\\folder");
	test_canonicalize(L"\\\\?\\UNC\\server\\C$\\folder\\..\\..\\..\\..\\share\\", L"\\\\?\\UNC\\server\\share");

	test_canonicalize(L"\\\\server\\share", L"\\\\server\\share");
	test_canonicalize(L"\\\\server\\share\\", L"\\\\server\\share");
	test_canonicalize(L"\\\\server\\share\\\\foo\\\\bar", L"\\\\server\\share\\foo\\bar");
	test_canonicalize(L"\\\\server\\\\share\\\\foo\\\\bar", L"\\\\server\\share\\foo\\bar");
	test_canonicalize(L"\\\\server\\share\\..\\foo", L"\\\\server\\foo");
	test_canonicalize(L"\\\\server\\..\\..\\share\\.\\foo", L"\\\\server\\share\\foo");
}
*/


char *str = "a\"/*b\"\\\\";
char c = '"', d = '\'';

/*\
foo();
*/
/\
* bar(); *\
/

int foo/*	*/(int/**/a, int b) {
	int f = a/*\*//b;
	int g = a/ /**/b;
}

int main( int /*argc is...*/ argc, char **argv /*argv is...*/ ) /**/ {
	
	char str0[]="/*  ML in string */"; // and after string??
	char str1[]=""\
	"0"\
	"1"\
	"2"\
	"3"\
	"4"\
	"5"\
	"6"\
	"7"\
	"8"\
	"9"\
	;

	char str3[]="//";
	char str4[]="/**/";
	char str5[]="\n\\\n\\\n\\";
	
	return (\
0);//OK /*OK*/
} // /**/

	// comment0
	
	/* comment1 */
	
	/*/////***********************************************
	   comment2
	***********************************************/////*/
	
	/*///////////////////////////////////////////////
	   comment3
	///////////////////////////////////////////////*/
	
	/*
	/////////////////////////////////////
	/////////////////////////////////////
	a\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
	/////////////////////////////////////
	b\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
	/////////////////////////////////////
	/////////////////////////////////////
	*/ // c d
	
	/*
		\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
		\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
		comment4
	*/
	
	// \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\ comment5
	
	// /**//**//**//**/ comment6
	// /* /* /* /* /* /* /* /* comment7
	
	/*/////////////////////// comment8 /////////////////////////*/
	
	/************************ comment 9 *************************/
	
	/* \\\\\\\\\\\\\\\\\\\\\\ comment 10 \\\\\\\\\\\\\\\\\\\\\\ */
	
/////   	int a=gl_0;
/////   	int b=2;
/////   	int c=a/\
/////   b;///ok?
/////   	int d=a+\
/////   b;/*and that??*/
	
	//ARGCC(1);
	//printf("%s", str0);

/\
\
\
\
\
/error0

//\
error1

//\

//int gl_0=1;
			
/*
error2="\/*  ML in string *\/";
error3=" // SL in string" \
			" // SL in string again";
*/

// // // // // error4="\/*  ML in string *\/";
// // // // // error5=" // SL in string" \
// // // // // 	      " // SL in string again";

// """""""""""""""""""""""""""""""""""""""""""""""""""""""""""""

/* """""""""""""""""""""""""""""""""""""""""""""""""""""""""" */

/* """""""""""""""""
"  -- == ++ -- == ++
"  -- == ++ -- == ++
"  -- == ++ -- == ++
"  -- == ++ -- == ++
"  -- == ++ -- == ++
"""""""""""""""""""" */

// /* ''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''
// /*  Some here. . . 
// /* ''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''


/*""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""
''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''
""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""*/

/* * / how much??? / * */

/*===
--- ///\\\///\\\///\\\///\\\/\/\/\/\/\/\//\\/////\\\\\/\/\/////\\\\/\/\
===*/

// // \\ \
//\
//\
//\
error6

/* /* how about that??? */
			
// /* or that? / // // /////// // // \\//\\\\///\			
			
/// char c1='\\'; /* === */
/// char c2='/'; // === ;""";""";""";""";""";

/*
#define ARGCC(X)\
X(0)\
X(1)\
X(2)\
X(3)
*/

																		 /*
0 /////////////////who care? 0 ***** //////////////////////////////////// 1
0 /////////////////who care? 0 ***** //////////////////////////////////// 1
0 /////////////////who care? 0 ***** //////////////////////////////////// 1
0 /////////////////who care? 0 ***** //////////////////////////////////// 1
0 /////////////////who care? 0 ***** //////////////////////////////////// 1
0 /////////////////who care? 0 ***** //////////////////////////////////// 1
0 /////////////////who care? 0 ***** //////////////////////////////////// 1
0 /////////////////who care? 0 ***** //////////////////////////////////// 1
0 /////////////////who care? 0 ***** //////////////////////////////////// 1
																		 */
//*////////////////who care? 0 ***** ////////////////////////////////////*/
//*////////////////who care? 0 ***** ////////////////////////////////////*/
//*////////////////who care? 0 ***** ////////////////////////////////////*/
//*////////////////who care? 0 ***** ////////////////////////////////////*/
//*////////////////who care? 0 ***** ////////////////////////////////////*/
//*////////////////who care? 0 ***** ////////////////////////////////////*/
//*////////////////who care? 0 ***** ////////////////////////////////////*/
//*////////////////who care? 0 ***** ////////////////////////////////////*/
//*////////////////who care? 0 ***** ////////////////////////////////////*/

/\
\
\
\
\
\
/x-1

/*\
*/


/**
    Returns the volume of a sphere with the specified radius.

    @param radius The radius of the circle.
    @return The volume of the sphere.
*/
			


//////////////////////////////////////////////////////////////////////////////



/*
********************************************************************
    
*/

/*


123   ///////////////////////////   /////////////// 000   000   000 /* /* /* /****** /////** 888
123   ///////////////////////////   /////////////// 000   000   000 /* /* /* /****** /////** 888
123   ///////////////////////////   /////////////// 000   000   000 /* /* /* /****** /////** 888
123   ///////////////////////////   /////////////// 000   000   000 /* /* /* /****** /////** 888
123   ///////////////////////////   /////////////// 000   000   000 /* /* /* /****** /////** 888
123   ///////////////////////////   /////////////// 000   000   000 /* /* /* /****** /////** 888
123   ///////////////////////////   /////////////// 000   000   000 /* /* /* /****** /////** 888
123   ///////////////////////////   /////////////// 000   000   000 /* /* /* /****** /////** 888
123   ///////////////////////////   /////////////// 000   000   000 /* /* /* /****** /////** 888
123   ///////////////////////////   /////////////// 000   000   000 /* /* /* /****** /////** 888
123   ///////////////////////////   /////////////// 000   000   000 /* /* /* /****** /////** 888
123   ///////////////////////////   /////////////// 000   000   000 /* /* /* /****** /////** 888
123   ///////////////////////////   /////////////// 000   000   000 /* /* /* /****** /////** 888
123   ///////////////////////////   /////////////// 000   000   000 /* /* /* /****** /////** 888
123   ///////////////////////////   /////////////// 000   000   000 /* /* /* /****** /////** 888
123   ///////////////////////////   /////////////// 000   000   000 /* /* /* /****** /////** 888
123   ///////////////////////////   /////////////// 000   000   000 /* /* /* /****** /////** 888
123   ///////////////////////////   /////////////// 000   000   000 /* /* /* /****** /////** 888
123   ///////////////////////////   /////////////// 000   000   000 /* /* /* /****** /////** 888




"""""""""""""""""""""""""""""""""""""""""
""""""""""""""""""""""""""""""""""""""""""
'"""""""""'"'"'"''

''';;;;;;;;;;;;;;;

;;;










*/




/*
This structure always exists immediately following any optional header in the COFF file (or following the file header, if f_opthdr is zero). When reading this header, you should read SCNHSZ bytes, and not rely on sizeof(SCNHDR) to give the correct size. The number of section headers present is given in the f_nscns field of the file header.
s_name - section name
The name of the section. The section name will never be more than eight characters, but be careful to handle the case where it's exactly eight characters - there will be no trailing null in the file! For shorter names, there field is padded with null bytes.
s_paddr - physical address of section data
This is the address at which the section data should be loaded into memory. For linked executables, this is the absolute address within the program space. For unlinked objects, this address is relative to the object's address space (i.e. the first section is always at offset zero).
s_vaddr - virtual address of section data
Always the same value as s_paddr in DJGPP.
s_size - section data size
The number of bytes of data stored in the file for this section. You should always read this many bytes from the file, beginning s_scnptr bytes from the beginning of the object.
s_scnptr - section data pointer
This contains the file offset of the section data.
s_relptr - relocation data pointer
The file offset of the relocation entries for this section.
s_lnnoptr - line number table pointer
The file offset of the line number entries for this section.
s_nreloc - number of relocation entries
The number of relocation entries for this section. Beware files with more than 65535 entries; this field truncates the value with no other way to get the "real" value.
s_nlnno - number of line number entries
The number of line number entries for this section. Beware files with more than 65535 entries; this field truncates the value with no other way to get the "real" value.
s_flags - flag bits
These flags provide additional information for each section. Flags other than those set below may be set, but are of no use aside from what these three provide.
*/

// /*\\\/*\\\/*\\\/****\\\\\\\////\\/\/\/\/\/\/\/\/\/\/\*\/\/\/\/\/\????\/\/\/\/\**\/\/\/\***\\\
// /*\\\/*\\\/*\\\/****\\\\\\\////\\/\/\/\/\/\/\/\/\/\/\*\/\/\/\/\/\????\/\/\/\/\**\/\/\/\***\\\
// /*\\\/*\\\/*\\\/****\\\\\\\////\\/\/\/\/\/\/\/\/\/\/\*\/\/\/\/\/\????\/\/\/\/\**\/\/\/\***\\\
// /*\\\/*\\\/*\\\/****\\\\\\\////\\/\/\/\/\/\/\/\/\/\/\*\/\/\/\/\/\????\/\/\/\/\**\/\/\/\***\\\
// /*\\\/*\\\/*\\\/****\\\\\\\////\\/\/\/\/\/\/\/\/\/\/\*\/\/\/\/\/\????\/\/\/\/\**\/\/\/\***\\\
// /*\\\/*\\\/*\\\/****\\\\\\\////\\/\/\/\/\/\/\/\/\/\/\*\/\/\/\/\/\????\/\/\/\/\**\/\/\/\***\\\
// /*\\\/*\\\/*\\\/****\\\\\\\////\\/\/\/\/\/\/\/\/\/\/\*\/\/\/\/\/\????\/\/\/\/\**\/\/\/\***\\\
// /*\\\/*\\\/*\\\/****\\\\\\\////\\/\/\/\/\/\/\/\/\/\/\*\/\/\/\/\/\????\/\/\/\/\**\/\/\/\***\\\
// /*\\\/*\\\/*\\\/****\\\\\\\////\\/\/\/\/\/\/\/\/\/\/\*\/\/\/\/\/\????\/\/\/\/\**\/\/\/\***\\\
// /*\\\/*\\\/*\\\/****\\\\\\\////\\/\/\/\/\/\/\/\/\/\/\*\/\/\/\/\/\????\/\/\/\/\**\/\/\/\***\\\
// /*\\\/*\\\/*\\\/****\\\\\\\////\\/\/\/\/\/\/\/\/\/\/\*\/\/\/\/\/\????\/\/\/\/\**\/\/\/\***\\\


// """"""""""""""""""""""''''''''''''''''''"""""""""""""""""""'''''''''''''''''"""""""""""""\\\\
// """"""""""""""""""""""''''''''''''''''''"""""""""""""""""""'''''''''''''''''"""""""""""""\\\\
// """"""""""""""""""""""''''''''''''''''''"""""""""""""""""""'''''''''''''''''"""""""""""""\\\\
// """"""""""""""""""""""''''''''''''''''''"""""""""""""""""""'''''''''''''''''"""""""""""""\\\\
// """"""""""""""""""""""''''''''''''''''''"""""""""""""""""""'''''''''''''''''"""""""""""""\\\\
// """"""""""""""""""""""''''''''''''''''''"""""""""""""""""""'''''''''''''''''"""""""""""""\\\\
// """"""""""""""""""""""''''''''''''''''''"""""""""""""""""""'''''''''''''''''"""""""""""""\\\\
// """"""""""""""""""""""''''''''''''''''''"""""""""""""""""""'''''''''''''''''"""""""""""""\\\\
// """"""""""""""""""""""''''''''''''''''''"""""""""""""""""""'''''''''''''''''"""""""""""""\\\\
// """"""""""""""""""""""''''''''''''''''''"""""""""""""""""""'''''''''''''''''"""""""""""""\\\\
// """"""""""""""""""""""''''''''''''''''''"""""""""""""""""""'''''''''''''''''"""""""""""""\\\\
// """"""""""""""""""""""''''''''''''''''''"""""""""""""""""""'''''''''''''''''"""""""""""""\\\\
// """"""""""""""""""""""''''''''''''''''''"""""""""""""""""""'''''''''''''''''"""""""""""""\\\\
// """"""""""""""""""""""''''''''''''''''''"""""""""""""""""""'''''''''''''''''"""""""""""""\\\\


/*
""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""\\\\\\\\\\\\
""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""\\\\\\\\\\\\
""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""\\\\\\\\\\\\
""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""\\\\\\\\\\\\
""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""\\\\\\\\\\\\
""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""\\\\\\\\\\\\
*/

/*
/////\\\///\\\""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""\\\\\\\\\\\\
/////\\\///\\\""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""\\\\\\\\\\\\
/////\\\///\\\""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""\\\\\\\\\\\\
/////\\\///\\\""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""\\\\\\\\\\\\
/////\\\///\\\""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""\\\\\\\\\\\\
/////\\\///\\\""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""\\\\\\\\\\\\
*/

/**** ********** **** ********** **** ********** **** ********** **** ********** **** ****
==========================================================================================
******************************************************************************************
/////\\\///\\\""""""""""""""""""""""""\\\\\\\\\\\\****************************************
0                                                 ****************************************
/////\\\///\\\""""""""""""""""""""""""\\\\\\\\\\\\****************************************
1                                                 ****************************************
/////\\\///\\\""""""""""""""""""""""""\\\\\\\\\\\\****************************************
2                                                 ****************************************
/////\\\///\\\""""""""""""""""""""""""\\\\\\\\\\\\****************************************
3                                                 ****************************************
/////\\\///\\\""""""""""""""""""""""""\\\\\\\\\\\\****************************************
0                                                 ****************************************
/////\\\///\\\""""""""""""""""""""""""\\\\\\\\\\\\****************************************
******************************************************************************************
==========================================================================================
**** ********** **** ********** **** ********** **** ********** **** ********** **** ****/


/*

"000000000000000000000000000000000000000000000000000000000000000000000000000"

"000000000000000000000000000000000000000000000000000000000000000000000000000"

"000000000000000000000000000000000000000000000000000000000000000000000000000"

*/

// "000000000000000000000000000000000000000000000000000000000000000000000000000"
// 
// "000000000000000000000000000000000000000000000000000000000000000000000000000"
// 
// "000000000000000000000000000000000000000000000000000000000000000000000000000"





























