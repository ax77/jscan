package cc.builders;

import static jscan.tokenize.T.T_COLON;

import java.util.ArrayList;
import java.util.List;

import cc.parse.Parse;
import cc.parse.ParseExpression;
import cc.tree.Expression;
import cc.types.CType;
import jscan.symtab.Keywords;
import jscan.tokenize.T;
import jscan.tokenize.Token;

class GenericAssociation {
  private final CType typename;
  private final Expression assignment;

  public GenericAssociation(CType typename, Expression assignment) {
    this.typename = typename;
    this.assignment = assignment;
  }

  public CType getTypename() {
    return typename;
  }

  public Expression getAssignment() {
    return assignment;
  }

}

class GenericSelection {
  // generic_selection
  //  : GENERIC '(' assignment_expression ',' generic_assoc_list ')'
  //  ;
  //
  //generic_assoc_list
  //  : generic_association
  //  | generic_assoc_list ',' generic_association
  //  ;
  //
  //generic_association
  //  : type_name ':' assignment_expression
  //  | DEFAULT ':' assignment_expression
  //  ;

  private final Expression controlExpression;
  private final List<GenericAssociation> associations;
  private Expression defaultAssociation;

  public GenericSelection(Expression controlExpression) {
    this.controlExpression = controlExpression;
    this.associations = new ArrayList<GenericAssociation>(0);
  }

  public void push(GenericAssociation e) {
    associations.add(e);
  }

  public Expression getControlExpression() {
    return controlExpression;
  }

  public List<GenericAssociation> getAssociations() {
    return associations;
  }

  public Expression getDefaultAssociation() {
    return defaultAssociation;
  }

  public void setDefaultAssociation(Expression defaultAssociation) {
    this.defaultAssociation = defaultAssociation;
  }

}

public class ApplyGenericResult {
  private final Parse parser;

  public ApplyGenericResult(Parse parser) {
    this.parser = parser;
  }

  public Expression getGenericResult(Token from) {
    return parseGenericSelection(from);
  }

  //generic_selection
  //  : GENERIC '(' assignment_expression ',' generic_assoc_list ')'
  //  ;
  //
  //generic_assoc_list
  //  : generic_association
  //  | generic_assoc_list ',' generic_association
  //  ;
  //
  //generic_association
  //  : type_name ':' assignment_expression
  //  | DEFAULT ':' assignment_expression
  //  ;

  private Expression parseGenericSelection(Token from) {
    Token id = parser.checkedMove(Keywords._Generic_ident);
    parser.lparen();

    Expression assignment = e_assign();
    parser.checkedMove(T.T_COMMA);

    GenericSelection genericSelection = new GenericSelection(assignment);
    parseGenericAssociation(genericSelection);

    while (parser.tp() == T.T_COMMA) {
      parser.move();
      parseGenericAssociation(genericSelection);
    }

    parser.rparen();

    Expression result = selectResultExpression(genericSelection);

    Expression e = new Expression(result, id);
    e.setResultType(result.getResultType());

    return e;
  }

  private void parseGenericAssociation(GenericSelection gs) {
    if (parser.tok().isIdent(Keywords.default_ident)) {

      if (gs.getDefaultAssociation() != null) {
        parser.perror("duplicate default in generic selection.");
      }

      parser.checkedMove(Keywords.default_ident);
      parser.checkedMove(T_COLON);
      gs.setDefaultAssociation(e_assign());
      return;
    }
    final CType typename = parser.parseTypename();
    parser.checkedMove(T_COLON);
    gs.push(new GenericAssociation(typename, e_assign()));
  }

  private Expression e_assign() {
    return new ParseExpression(parser).e_assign();
  }

  private Expression selectResultExpression(GenericSelection genericSelection) {
    for (GenericAssociation e : genericSelection.getAssociations()) {
      ApplyExpressionType.applytype(e.getAssignment(), TypeApplierStage.stage_start);
    }

    ApplyExpressionType.applytype(genericSelection.getControlExpression(), TypeApplierStage.generic_control_expr);
    if (genericSelection.getDefaultAssociation() != null) {
      ApplyExpressionType.applytype(genericSelection.getDefaultAssociation(), TypeApplierStage.stage_start);
    }

    CType need = genericSelection.getControlExpression().getResultType();
    if (need == null) {
      parser.perror("no type for control expression.");
    }

    for (GenericAssociation assoc : genericSelection.getAssociations()) {
      if (assoc.getTypename().isEqualTo(need)) {
        return assoc.getAssignment();
      }
    }

    final Expression defaultAssociation = genericSelection.getDefaultAssociation();
    if (defaultAssociation == null) {
      parser.perror("you need specify default association for this type: " + need.toString());
    }

    return defaultAssociation;
  }

}
