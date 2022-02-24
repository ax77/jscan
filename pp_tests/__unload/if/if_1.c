#define a
#ifdef a
	"1{"
	"a ifdef is ok"
	#if 1
		"1.1{"
		"1 ok too"
		#if 2
			"1.1.1{"
			"2 ok too"
			"1.1.1}"
			#if 3
				"3.3.3.3.3{{{{{"
				#if 4
					#ifdef a
						"122222"
						#if 1
							#if 1
								"-------------------------------------------"
								#ifdef a
									#ifndef b
										";;;;;"
										#define b
										#ifdef b
											"b defined ok"
										#endif
									#endif
								#endif
							#endif
						#endif
					#endif
				#endif
				"3.3.3.3.3}}}}}"
			#endif
		#endif
		"1.1}"
	#endif
	"1}"
#elif 4-4
	"4-2"
#elif 3-2
	"3-2"
#else
	"ok"
#endif
#define xxx 32
xxx
yyy
zzz
