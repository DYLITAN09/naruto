package naruto.visitor;

import naruto.model.Ninja;
import naruto.model.Mission;

public interface NarutoVisitor {
    void visit(Ninja ninja);

    void visit(Mission mission);
}
