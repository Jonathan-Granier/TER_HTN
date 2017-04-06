(define (problem schedule-42-4)
(:domain schedule)
(:objects
    S1
    R1
    P1
    Q1
    O1
    N1
    M1
    L1
    K1
    J1
    I1
    H1
    G1
    F1
    E1
    D1
    C1
    B1
    A1
    Z0
    W0
    V0
    U0
    S0
    R0
    P0
    Q0
    O0
    N0
    M0
    L0
    K0
    J0
    I0
    H0
    G0
    F0
    E0
    D0
    C0
    CIRCULAR
    TWO
    THREE
    BLUE
    YELLOW
    BACK
    RED
    B0
    FRONT
    ONE
    BLACK
    OBLONG
    A0
)
(:init
    (idle punch) (idle drill-press) (idle lathe) (idle roller) (idle polisher)
    (idle immersion-painter) (idle spray-painter) (idle grinder) (ru unwantedargs)
    (SHAPE A0 CIRCULAR)
    (SURFACE-CONDITION A0 SMOOTH)
    (PAINTED A0 BLACK)
    (HAS-HOLE A0 TWO FRONT) (lasthole A0 TWO FRONT) (linked A0 nowidth noorient TWO FRONT)
    (TEMPERATURE A0 COLD)
    (SHAPE B0 OBLONG)
    (SURFACE-CONDITION B0 ROUGH)
    (PAINTED B0 BLUE)
    (HAS-HOLE B0 TWO FRONT) (lasthole B0 TWO FRONT) (linked B0 nowidth noorient TWO FRONT)
    (TEMPERATURE B0 COLD)
    (SHAPE C0 CYLINDRICAL)
    (SURFACE-CONDITION C0 SMOOTH)
    (PAINTED C0 RED)
    (HAS-HOLE C0 TWO FRONT) (lasthole C0 TWO FRONT) (linked C0 nowidth noorient TWO FRONT)
    (TEMPERATURE C0 COLD)
    (SHAPE D0 OBLONG)
    (SURFACE-CONDITION D0 POLISHED)
    (PAINTED D0 YELLOW)
    (HAS-HOLE D0 ONE BACK) (lasthole D0 ONE BACK) (linked D0 nowidth noorient ONE BACK)
    (TEMPERATURE D0 COLD)
    (SHAPE E0 CYLINDRICAL)
    (SURFACE-CONDITION E0 ROUGH)
    (PAINTED E0 BLUE)
    (HAS-HOLE E0 THREE FRONT) (lasthole E0 THREE FRONT) (linked E0 nowidth noorient THREE FRONT)
    (TEMPERATURE E0 COLD)
    (SHAPE F0 CIRCULAR)
    (SURFACE-CONDITION F0 POLISHED)
    (PAINTED F0 YELLOW)
    (HAS-HOLE F0 TWO FRONT) (lasthole F0 TWO FRONT) (linked F0 nowidth noorient TWO FRONT)
    (TEMPERATURE F0 COLD)
    (SHAPE G0 OBLONG)
    (SURFACE-CONDITION G0 ROUGH)
    (PAINTED G0 BLUE)
    (HAS-HOLE G0 ONE BACK) (lasthole G0 ONE BACK) (linked G0 nowidth noorient ONE BACK)
    (TEMPERATURE G0 COLD)
    (SHAPE H0 OBLONG)
    (SURFACE-CONDITION H0 SMOOTH)
    (PAINTED H0 RED)
    (HAS-HOLE H0 ONE FRONT) (lasthole H0 ONE FRONT) (linked H0 nowidth noorient ONE FRONT)
    (TEMPERATURE H0 COLD)
    (SHAPE I0 CYLINDRICAL)
    (SURFACE-CONDITION I0 ROUGH)
    (PAINTED I0 YELLOW)
    (HAS-HOLE I0 THREE FRONT) (lasthole I0 THREE FRONT) (linked I0 nowidth noorient THREE FRONT)
    (TEMPERATURE I0 COLD)
    (SHAPE J0 CIRCULAR)
    (SURFACE-CONDITION J0 POLISHED)
    (PAINTED J0 BLUE)
    (HAS-HOLE J0 ONE FRONT) (lasthole J0 ONE FRONT) (linked J0 nowidth noorient ONE FRONT)
    (TEMPERATURE J0 COLD)
    (SHAPE K0 CIRCULAR)
    (SURFACE-CONDITION K0 ROUGH)
    (PAINTED K0 BLUE)
    (HAS-HOLE K0 ONE FRONT) (lasthole K0 ONE FRONT) (linked K0 nowidth noorient ONE FRONT)
    (TEMPERATURE K0 COLD)
    (SHAPE L0 CIRCULAR)
    (SURFACE-CONDITION L0 POLISHED)
    (PAINTED L0 BLUE)
    (HAS-HOLE L0 THREE BACK) (lasthole L0 THREE BACK) (linked L0 nowidth noorient THREE BACK)
    (TEMPERATURE L0 COLD)
    (SHAPE M0 CYLINDRICAL)
    (SURFACE-CONDITION M0 ROUGH)
    (PAINTED M0 BLUE)
    (HAS-HOLE M0 ONE FRONT) (lasthole M0 ONE FRONT) (linked M0 nowidth noorient ONE FRONT)
    (TEMPERATURE M0 COLD)
    (SHAPE N0 CYLINDRICAL)
    (SURFACE-CONDITION N0 ROUGH)
    (PAINTED N0 YELLOW)
    (HAS-HOLE N0 TWO FRONT) (lasthole N0 TWO FRONT) (linked N0 nowidth noorient TWO FRONT)
    (TEMPERATURE N0 COLD)
    (SHAPE O0 CYLINDRICAL)
    (SURFACE-CONDITION O0 POLISHED)
    (PAINTED O0 RED)
    (HAS-HOLE O0 ONE FRONT) (lasthole O0 ONE FRONT) (linked O0 nowidth noorient ONE FRONT)
    (TEMPERATURE O0 COLD)
    (SHAPE Q0 CYLINDRICAL)
    (SURFACE-CONDITION Q0 SMOOTH)
    (PAINTED Q0 BLUE)
    (HAS-HOLE Q0 THREE FRONT) (lasthole Q0 THREE FRONT) (linked Q0 nowidth noorient THREE FRONT)
    (TEMPERATURE Q0 COLD)
    (SHAPE P0 OBLONG)
    (SURFACE-CONDITION P0 ROUGH)
    (PAINTED P0 YELLOW)
    (HAS-HOLE P0 THREE FRONT) (lasthole P0 THREE FRONT) (linked P0 nowidth noorient THREE FRONT)
    (TEMPERATURE P0 COLD)
    (SHAPE R0 CYLINDRICAL)
    (SURFACE-CONDITION R0 SMOOTH)
    (PAINTED R0 BLUE)
    (HAS-HOLE R0 THREE BACK) (lasthole R0 THREE BACK) (linked R0 nowidth noorient THREE BACK)
    (TEMPERATURE R0 COLD)
    (SHAPE S0 CIRCULAR)
    (SURFACE-CONDITION S0 POLISHED)
    (PAINTED S0 RED)
    (HAS-HOLE S0 ONE FRONT) (lasthole S0 ONE FRONT) (linked S0 nowidth noorient ONE FRONT)
    (TEMPERATURE S0 COLD)
    (SHAPE U0 CYLINDRICAL)
    (SURFACE-CONDITION U0 SMOOTH)
    (PAINTED U0 RED)
    (HAS-HOLE U0 ONE BACK) (lasthole U0 ONE BACK) (linked U0 nowidth noorient ONE BACK)
    (TEMPERATURE U0 COLD)
    (SHAPE V0 OBLONG)
    (SURFACE-CONDITION V0 ROUGH)
    (PAINTED V0 BLUE)
    (HAS-HOLE V0 TWO FRONT) (lasthole V0 TWO FRONT) (linked V0 nowidth noorient TWO FRONT)
    (TEMPERATURE V0 COLD)
    (SHAPE W0 CIRCULAR)
    (SURFACE-CONDITION W0 ROUGH)
    (PAINTED W0 BLUE)
    (HAS-HOLE W0 THREE FRONT) (lasthole W0 THREE FRONT) (linked W0 nowidth noorient THREE FRONT)
    (TEMPERATURE W0 COLD)
    (SHAPE Z0 CYLINDRICAL)
    (SURFACE-CONDITION Z0 POLISHED)
    (PAINTED Z0 BLUE)
    (HAS-HOLE Z0 ONE FRONT) (lasthole Z0 ONE FRONT) (linked Z0 nowidth noorient ONE FRONT)
    (TEMPERATURE Z0 COLD)
    (SHAPE A1 CYLINDRICAL)
    (SURFACE-CONDITION A1 SMOOTH)
    (PAINTED A1 BLACK)
    (HAS-HOLE A1 THREE FRONT) (lasthole A1 THREE FRONT) (linked A1 nowidth noorient THREE FRONT)
    (TEMPERATURE A1 COLD)
    (SHAPE B1 CYLINDRICAL)
    (SURFACE-CONDITION B1 POLISHED)
    (PAINTED B1 YELLOW)
    (HAS-HOLE B1 TWO FRONT) (lasthole B1 TWO FRONT) (linked B1 nowidth noorient TWO FRONT)
    (TEMPERATURE B1 COLD)
    (SHAPE C1 CYLINDRICAL)
    (SURFACE-CONDITION C1 ROUGH)
    (PAINTED C1 YELLOW)
    (HAS-HOLE C1 TWO FRONT) (lasthole C1 TWO FRONT) (linked C1 nowidth noorient TWO FRONT)
    (TEMPERATURE C1 COLD)
    (SHAPE D1 CYLINDRICAL)
    (SURFACE-CONDITION D1 SMOOTH)
    (PAINTED D1 BLUE)
    (HAS-HOLE D1 THREE FRONT) (lasthole D1 THREE FRONT) (linked D1 nowidth noorient THREE FRONT)
    (TEMPERATURE D1 COLD)
    (SHAPE E1 CIRCULAR)
    (SURFACE-CONDITION E1 POLISHED)
    (PAINTED E1 BLUE)
    (HAS-HOLE E1 THREE FRONT) (lasthole E1 THREE FRONT) (linked E1 nowidth noorient THREE FRONT)
    (TEMPERATURE E1 COLD)
    (SHAPE F1 CYLINDRICAL)
    (SURFACE-CONDITION F1 POLISHED)
    (PAINTED F1 BLUE)
    (HAS-HOLE F1 ONE BACK) (lasthole F1 ONE BACK) (linked F1 nowidth noorient ONE BACK)
    (TEMPERATURE F1 COLD)
    (SHAPE G1 OBLONG)
    (SURFACE-CONDITION G1 POLISHED)
    (PAINTED G1 RED)
    (HAS-HOLE G1 TWO BACK) (lasthole G1 TWO BACK) (linked G1 nowidth noorient TWO BACK)
    (TEMPERATURE G1 COLD)
    (SHAPE H1 CYLINDRICAL)
    (SURFACE-CONDITION H1 SMOOTH)
    (PAINTED H1 BLACK)
    (HAS-HOLE H1 TWO FRONT) (lasthole H1 TWO FRONT) (linked H1 nowidth noorient TWO FRONT)
    (TEMPERATURE H1 COLD)
    (SHAPE I1 CYLINDRICAL)
    (SURFACE-CONDITION I1 ROUGH)
    (PAINTED I1 YELLOW)
    (HAS-HOLE I1 ONE BACK) (lasthole I1 ONE BACK) (linked I1 nowidth noorient ONE BACK)
    (TEMPERATURE I1 COLD)
    (SHAPE J1 CYLINDRICAL)
    (SURFACE-CONDITION J1 SMOOTH)
    (PAINTED J1 YELLOW)
    (HAS-HOLE J1 THREE FRONT) (lasthole J1 THREE FRONT) (linked J1 nowidth noorient THREE FRONT)
    (TEMPERATURE J1 COLD)
    (SHAPE K1 OBLONG)
    (SURFACE-CONDITION K1 ROUGH)
    (PAINTED K1 BLACK)
    (HAS-HOLE K1 ONE FRONT) (lasthole K1 ONE FRONT) (linked K1 nowidth noorient ONE FRONT)
    (TEMPERATURE K1 COLD)
    (SHAPE L1 CYLINDRICAL)
    (SURFACE-CONDITION L1 SMOOTH)
    (PAINTED L1 YELLOW)
    (HAS-HOLE L1 THREE FRONT) (lasthole L1 THREE FRONT) (linked L1 nowidth noorient THREE FRONT)
    (TEMPERATURE L1 COLD)
    (SHAPE M1 CYLINDRICAL)
    (SURFACE-CONDITION M1 POLISHED)
    (PAINTED M1 BLACK)
    (HAS-HOLE M1 THREE FRONT) (lasthole M1 THREE FRONT) (linked M1 nowidth noorient THREE FRONT)
    (TEMPERATURE M1 COLD)
    (SHAPE N1 CIRCULAR)
    (SURFACE-CONDITION N1 ROUGH)
    (PAINTED N1 BLUE)
    (HAS-HOLE N1 THREE BACK) (lasthole N1 THREE BACK) (linked N1 nowidth noorient THREE BACK)
    (TEMPERATURE N1 COLD)
    (SHAPE O1 CIRCULAR)
    (SURFACE-CONDITION O1 SMOOTH)
    (PAINTED O1 BLACK)
    (HAS-HOLE O1 TWO BACK) (lasthole O1 TWO BACK) (linked O1 nowidth noorient TWO BACK)
    (TEMPERATURE O1 COLD)
    (SHAPE Q1 CIRCULAR)
    (SURFACE-CONDITION Q1 POLISHED)
    (PAINTED Q1 BLUE)
    (HAS-HOLE Q1 TWO BACK) (lasthole Q1 TWO BACK) (linked Q1 nowidth noorient TWO BACK)
    (TEMPERATURE Q1 COLD)
    (SHAPE P1 CYLINDRICAL)
    (SURFACE-CONDITION P1 POLISHED)
    (PAINTED P1 BLUE)
    (HAS-HOLE P1 ONE FRONT) (lasthole P1 ONE FRONT) (linked P1 nowidth noorient ONE FRONT)
    (TEMPERATURE P1 COLD)
    (SHAPE R1 CYLINDRICAL)
    (SURFACE-CONDITION R1 POLISHED)
    (PAINTED R1 RED)
    (HAS-HOLE R1 THREE FRONT) (lasthole R1 THREE FRONT) (linked R1 nowidth noorient THREE FRONT)
    (TEMPERATURE R1 COLD)
    (SHAPE S1 OBLONG)
    (SURFACE-CONDITION S1 POLISHED)
    (PAINTED S1 BLACK)
    (HAS-HOLE S1 THREE BACK) (lasthole S1 THREE BACK) (linked S1 nowidth noorient THREE BACK)
    (TEMPERATURE S1 COLD)
    (CAN-ORIENT DRILL-PRESS BACK)
    (CAN-ORIENT PUNCH BACK)
    (CAN-ORIENT DRILL-PRESS FRONT)
    (CAN-ORIENT PUNCH FRONT)
    (HAS-PAINT IMMERSION-PAINTER YELLOW)
    (HAS-PAINT SPRAY-PAINTER YELLOW)
    (HAS-PAINT IMMERSION-PAINTER BLUE)
    (HAS-PAINT SPRAY-PAINTER BLUE)
    (HAS-PAINT IMMERSION-PAINTER BLACK)
    (HAS-PAINT SPRAY-PAINTER BLACK)
    (HAS-PAINT IMMERSION-PAINTER RED)
    (HAS-PAINT SPRAY-PAINTER RED)
    (HAS-BIT DRILL-PRESS THREE)
    (HAS-BIT PUNCH THREE)
    (HAS-BIT DRILL-PRESS TWO)
    (HAS-BIT PUNCH TWO)
    (HAS-BIT DRILL-PRESS ONE)
    (HAS-BIT PUNCH ONE)
    (PART S1) (unscheduled S1)
    (PART R1) (unscheduled R1)
    (PART P1) (unscheduled P1)
    (PART Q1) (unscheduled Q1)
    (PART O1) (unscheduled O1)
    (PART N1) (unscheduled N1)
    (PART M1) (unscheduled M1)
    (PART L1) (unscheduled L1)
    (PART K1) (unscheduled K1)
    (PART J1) (unscheduled J1)
    (PART I1) (unscheduled I1)
    (PART H1) (unscheduled H1)
    (PART G1) (unscheduled G1)
    (PART F1) (unscheduled F1)
    (PART E1) (unscheduled E1)
    (PART D1) (unscheduled D1)
    (PART C1) (unscheduled C1)
    (PART B1) (unscheduled B1)
    (PART A1) (unscheduled A1)
    (PART Z0) (unscheduled Z0)
    (PART W0) (unscheduled W0)
    (PART V0) (unscheduled V0)
    (PART U0) (unscheduled U0)
    (PART S0) (unscheduled S0)
    (PART R0) (unscheduled R0)
    (PART P0) (unscheduled P0)
    (PART Q0) (unscheduled Q0)
    (PART O0) (unscheduled O0)
    (PART N0) (unscheduled N0)
    (PART M0) (unscheduled M0)
    (PART L0) (unscheduled L0)
    (PART K0) (unscheduled K0)
    (PART J0) (unscheduled J0)
    (PART I0) (unscheduled I0)
    (PART H0) (unscheduled H0)
    (PART G0) (unscheduled G0)
    (PART F0) (unscheduled F0)
    (PART E0) (unscheduled E0)
    (PART D0) (unscheduled D0)
    (PART C0) (unscheduled C0)
    (PART B0) (unscheduled B0)
    (PART A0) (unscheduled A0)
)
(:goal (and
    (PAINTED S0 BLACK)
    (PAINTED O0 YELLOW)
    (PAINTED V0 RED)
    (PAINTED Z0 YELLOW)
    (SURFACE-CONDITION H0 POLISHED)
    (SURFACE-CONDITION A0 POLISHED)
    (PAINTED A1 BLUE)
    (SURFACE-CONDITION G1 ROUGH)
    (SURFACE-CONDITION M0 POLISHED)
    (SURFACE-CONDITION P1 SMOOTH)
    (SURFACE-CONDITION A1 POLISHED)
    (SURFACE-CONDITION E1 ROUGH)
    (SURFACE-CONDITION D1 ROUGH)
    (SURFACE-CONDITION O1 POLISHED)
    (PAINTED E1 YELLOW)
    (PAINTED O1 BLUE)
    (PAINTED B0 RED)
    (PAINTED A0 BLUE)
    (SURFACE-CONDITION R1 ROUGH)
    (SURFACE-CONDITION G0 SMOOTH)
    (SHAPE J0 CYLINDRICAL)
    (SURFACE-CONDITION V0 POLISHED)
    (PAINTED K0 BLACK)
    (PAINTED J1 BLUE)
    (PAINTED N0 BLUE)
    (SHAPE V0 CYLINDRICAL)
    (SURFACE-CONDITION K1 SMOOTH)
    (PAINTED R1 YELLOW)
    (SHAPE G1 CYLINDRICAL)
    (SHAPE L0 CYLINDRICAL)
    (SHAPE S1 CYLINDRICAL)
    (SURFACE-CONDITION C1 POLISHED)
    (SURFACE-CONDITION R0 ROUGH)
    (SURFACE-CONDITION B1 ROUGH)
    (SURFACE-CONDITION E0 SMOOTH)
    (SHAPE K0 CYLINDRICAL)
    (SURFACE-CONDITION F0 ROUGH)
    (PAINTED C1 RED)
    (PAINTED Q1 RED)
    (SHAPE K1 CYLINDRICAL)
    (SURFACE-CONDITION N0 SMOOTH)
    (SHAPE P0 CYLINDRICAL)
)))
