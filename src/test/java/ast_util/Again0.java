package ast_util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import ast.flat.func.ExecFlowItem;
import ast.flat.func.RewriteStmt;
import ast.main.ParserMain;
import ast.parse.Parse;
import ast.tree.Statement;
import ast.tree.TranslationUnit;
import jscan.fio.FileReadKind;
import jscan.fio.FileWrapper;
import jscan.utils.GlobalCounter;

public class Again0 {


  class BasicBlock {
    private final List<ExecFlowItem> items;

    public BasicBlock() {
      this.items = new ArrayList<>();
    }

    public void add(ExecFlowItem e) {
      items.add(e);
    }

    public List<ExecFlowItem> getItems() {
      return items;
    }

    public boolean isEmpty() {
      return items.isEmpty();
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder();
      for (ExecFlowItem item : items) {
        if (item.isLeader()) {
          sb.append(item.getBasicBlockId() + ":\n");
        }
        sb.append(item.toString() + "\n");
      }
      return sb.toString();
    }
  }

  private void identifyLeaders(List<ExecFlowItem> items) {

    /// 1) It is the first instruction. The first instruction is a leader.
    /// 2) The target of a conditional or an unconditional goto/jump instruction is a leader.
    /// 3) The instruction that immediately follows a conditional or an unconditional goto/jump instruction is a leader.
    /// 

    // 1) 
    int size = items.size();
    if (size > 0) {
      items.get(0).setLeader(true);
    }

    // 2) mark all targets
    // begin with the '1' index, because the first instruction is always a leader.
    //
    for (int i = 1; i < size; i++) {
      ExecFlowItem curr = items.get(i);
      ExecFlowItem next = ((i + 1) < size) ? items.get(i + 1) : null;
      if (curr.isLabel()) {
        if (next != null) {
          next.setLeader(true);
          continue;
        }
      }
    }

    // 3) mark all jumps
    // begin with the '1' index, because the first instruction is always a leader.
    //
    for (int i = 1; i < size; i++) {
      ExecFlowItem curr = items.get(i);
      ExecFlowItem next = ((i + 1) < size) ? items.get(i + 1) : null;
      if (curr.isAnyJmp()) {
        if (next != null) {
          next.setLeader(true);
          continue;
        }
      }
    }

    // assign identifiers
    for (ExecFlowItem item : items) {
      if (item.isLabel()) {
        continue;
      }
      if (!item.isLeader()) {
        continue;
      }
      item.setBasicBlockId(GlobalCounter.nextLabel("BB_"));
    }
  }

  private List<BasicBlock> buildBlocks(List<ExecFlowItem> items) {
    List<BasicBlock> result = new ArrayList<>();
    BasicBlock block = new BasicBlock();

    while (!items.isEmpty()) {
      ExecFlowItem curr = items.remove(0);
      ExecFlowItem next = items.isEmpty() ? null : items.get(0);
      if (next != null && next.isLeader()) {
        block.add(curr);
        result.add(block);
        block = new BasicBlock();
        continue;
      }
      block.add(curr);
    }

    if (!block.isEmpty()) {
      result.add(block);
    }

    return result;
  }

  @Test
  public void test0() throws IOException {

    String dir = System.getProperty("user.dir");
    String txt = new FileWrapper(dir + "/cc_tests/01.c").readToString(FileReadKind.APPEND_LF);

    Parse p = new Parse(new ParserMain(new StringBuilder(txt)).preprocess());
    TranslationUnit unit = p.parse_unit();

    final Statement block = unit.getExternalDeclarations().get(0).getFunctionDefinition().getBlock();
    RewriteStmt stmt = new RewriteStmt();
    stmt.genStmt(block);

    List<ExecFlowItem> items = stmt.getItems();
    identifyLeaders(items);

    List<BasicBlock> blocks = buildBlocks(items);
    for (BasicBlock b : blocks) {
      System.out.println(b);
    }

  }

}
