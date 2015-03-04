CREATE OR REPLACE PROCEDURE PROC_4(P1 IN VARCHAR2, P2 IN OUT VARCHAR2, P3 OUT VARCHAR2) AS
BEGIN
  dbms_output.put_line('proc4 params: p1 = ' || p1 || ', p2 = ' || p2);
  p3 := p1 || ' & ' || p2;
  p2 := p1;
  dbms_output.put_line('new params: p1 = ' || p1 || ', p2 = ' || p2 || ', p3 = ' || p3);
END PROC_4;
/

CREATE OR REPLACE FUNCTION FUNC_5(P1 IN VARCHAR2, P2 IN OUT VARCHAR2, P3 OUT VARCHAR2) RETURN VARCHAR2 AS
BEGIN
  dbms_output.put_line('proc4 params: p1 = ' || p1 || ', p2 = ' || p2);
  p3 := p1 || ' & ' || p2;
  p2 := p1;
  dbms_output.put_line('new params: p1 = ' || p1 || ', p2 = ' || p2 || ', p3 = ' || p3);

  RETURN 'returns ' || p1 || ' & ' || p2 || ' + ' || p3;
END FUNC_5;
/