// https://social.msdn.microsoft.com/Forums/vstudio/en-US/23f682bf-8421-474a-aa38-e32f9a0b728f/difference-between-microsoft-visual-studio-preprocessor-and-gccs?forum=vclanguage

# define M3(x, y, z) x + y + z
# define M2(x, y) M3(x, y)
# define P(x, y) {x, y}
# define M(x, y) M2(x, P(x, y))
M(a, b)
