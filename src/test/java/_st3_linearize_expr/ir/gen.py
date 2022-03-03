opcodes = """
    AssignVarAllocObject               ,
    AssignVarBinop                     ,
    AssignVarBool                      ,
    AssignVarFieldAccess               ,
    AssignVarStaticLabel               ,
    AssignVarSizeof                    ,
    AssignVarFlatCallResult            ,
    AssignVarNum                       ,
    AssignVarUnop                      ,
    AssignVarVar                       ,
    AssignVarCastExpression            ,
    FlatCallVoid                       ,
    SelectionShortCircuit              ,
    BuiltinFuncAssertTrue              ,
    StoreFieldLiteral                  ,
    StoreVarLiteral
                            
"""
opcodes_arr = []
for opc in opcodes.split(','):
    opcodes_arr.append(opc.strip())

opcodes_arr.sort()


fields = ''
constructors = ''
methods = ''
to_strings = '  @Override\n  public String toString() {\n'
to_strings += '    if(ignore) { return \"\"; }\n'

getters = '  public Opc getOpcode() { return this.opcode; }\n'
get_dest  = '  public Var getDest() {\n'
get_dest += '    if(ignore) { return rvalueDestWrapper; }\n'

new_hdr = """
  private final String uid;
  private boolean ignore;
  private Var rvalueDestWrapper;
  public boolean isIgnore() {
    return ignore;
  }
  public void setIgnore(Var rvalueDestWrapper) { // this dest is only for returns, the result
    this.ignore = true;
    this.rvalueDestWrapper = rvalueDestWrapper;
  }
  private String genUid() {
    return UUID.randomUUID().toString();
  }
  @Override
  public int hashCode() {
    return Objects.hash(uid);
  }
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    FlatCodeItem other = (FlatCodeItem) obj;
    return Objects.equals(uid, other.uid);
  }
"""

get_all_vars = '  public List<Var> getAllVars() {\n'
get_lvalue = '  public Var getLvalue() {\n'

assigns_ops = """
  public boolean isOneOfAssigns() {
      return 
         isAssignVarAllocObject()  
      || isAssignVarBinop()  
      || isAssignVarBool()  
      || isAssignVarFieldAccess() 
      || isAssignVarStaticLabel()  
      || isAssignVarFlatCallResult()
      || isAssignVarNum() 
      || isAssignVarUnop()   
      || isAssignVarVar()
      || isAssignVarCastExpression() 
      ;
  }
"""

for opc in opcodes_arr:
    varname = opc[0].lower() + opc[1:]
    fields += '  private ' + opc + ' ' + varname + ';\n'
    constructors += '  public FlatCodeItem(' + opc + ' ' + varname + ') { this.uid = genUid(); this.opcode = Opc.' + opc + '; this.' + varname + ' = ' + varname + '; }\n'
    methods += '  public boolean is' + opc + '() { return this.opcode == Opc.' + opc + '; }\n'
    to_strings += '    if(is' + opc + '()) { return ' + varname + '.toString(); }\n'
    getters += '  public ' + opc + ' get' + opc + '() { return this.' + varname + '; }\n'
    
    get_all_vars += '    if ( is' + opc + '() ) { return ' + varname + '.getAllVars(); }\n'
    
    if str(opc).startswith('Assign'):
        get_lvalue += '    if ( is' + opc + '() ) { return ' + varname + '.getLvalue(); }\n'
    
    get_dest += '    if(is' + opc + '()) { '
    if str(opc).startswith('Assign'):
        get_dest += 'return ' + varname + '.getLvalue();'
    elif str(opc) == 'FlatCallConstructor':
        get_dest += 'return ' + varname + '.getThisVar();'
    elif str(opc) == 'IntrinsicText' or str(opc) == 'SelectionShortCircuit':
        get_dest += 'return ' + varname + '.getDest();'
    elif str(opc) == 'CallListenerResultMethod':
        get_dest += 'return ' + varname + '.getDest();'
    else:
        get_dest += 'err();'
        
    get_dest += ' }\n'

print('  //generated code begin')
print('  //@formatter:off')
print(fields)
print(new_hdr)
print(constructors)
print(methods)

get_all_vars += '    err();\n'
get_all_vars += '    return null;\n'
get_all_vars += '  }\n'
print(get_all_vars)

get_lvalue += '    err();\n'
get_lvalue += '    return null;\n'
get_lvalue += '  }\n'
print(get_lvalue)

to_strings += '    return \"?UnknownItem\"; \n  }\n';
print(to_strings)

print(getters)
print(assigns_ops)

get_dest += '    throw new AstParseException(\"unknown item for result: \" + toString());\n'
get_dest += '  }\n'
get_dest += '  private void err() { throw new AstParseException(\"unexpected item for result: \" + toString()); }'


print(get_dest)
print('  //@formatter:on')
print('  //generated code end')























