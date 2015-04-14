/*
 * analyseur syntaxique  d'un log apache
 *
 * auteurs : BURTEAUX CHAUDOIN MATHIEU NEVES PAPELIER POTIER
 */

package fr.ul.miage.exemple.generated;

import fr.ul.miage.exemple.Arbre.*;
import fr.ul.miage.exemple.TDS.*;

import java_cup.runtime.Symbol;
import fr.ul.miage.exemple.*;
import java.util.*;
import java.text.*;
import java.sql.*;
import java.util.concurrent.TimeUnit;


%%



/* options */
%line
%public
%cupsym ParserSym
%cup

/* macros */

IF = (" "|\t)*(if|IF)(" "|\t)*
ELSE = (" "|\t)*(else|ELSE)(" "|\t)*
WHILE = (" "|\t)*(while|WHILE)(" "|\t)*

NUM	= (" "|\t)*[0-9]+(" "|\t)*
OPBOOL = (" "|\t)*(==|<|>|<=|>=|=\!|or|OR|and|AND|\!)(" "|\t)*
OP = (" "|\t)*(\+|\*|\/|-)(" "|\t)*
TYPE = (" "|\t)*(int|INT|boolean|BOOLEAN)(" "|\t)*
RETURN = (" "|\t)*(return|RETURN)(" "|\t)*
VOID = (" "|\t)*(void|VOID)(" "|\t)*
VAR = (" "|\t)*[a-zA-Z][a-zA-Z0-9]*(" "|\t)*
PV = (" "|\t)*;(" "|\t)*
AO = (" "|\t|\r\n|\n|\r)*"{"(" "|\t|\r\n|\n|\r)*
AF = (" "|\t|\r\n|\n|\r)*"}"(" "|\t|\r\n|\n|\r)*
PO = (" "|\t)*"("(" "|\t)*
PF = (" "|\t)*")"(" "|\t)*
NL = (" "|\t)*(\r\n|\n|\r)(" "|\t)*
EQ = (" "|\t)*"="(" "|\t)*
VRG = (" "|\t)*","(" "|\t)*
COM = (" "|\t)*\/\*([^*]|[\r\n]|(\*+([^*/]|[\r\n])))*\*+\/(" "|\t)*

%{

%}

%eof{

%eof}

%%

/* regles */

{ELSE}			{ Main.print("else:"+new Noeud("CST",yytext())+"#");return new Symbol(ParserSym.ELSE, new Noeud("ELSE"));}
{IF}         { Main.print("if:"+new Noeud("CST",yytext())+"#");return new Symbol(ParserSym.IF, new Noeud("IF"));}
{WHILE}         { Main.print("while:"+new Noeud("CST",yytext())+"#");return new Symbol(ParserSym.WHILE, new Noeud("WHILE"));}

{PV}         { Main.print("pv:"+new Noeud("CST",yytext())+"#");return new Symbol(ParserSym.PV, yytext());}
{AO}         { Main.print("ao:"+new Noeud("CST",yytext())+"#");return new Symbol(ParserSym.AO, yytext());}
{AF}         { Main.print("af:"+new Noeud("CST",yytext())+"#");return new Symbol(ParserSym.AF, yytext());}
{PO}         { Main.print("po:"+new Noeud("CST",yytext())+"#");return new Symbol(ParserSym.PO, yytext());}
{PF}         { Main.print("pf:"+new Noeud("CST",yytext())+"#");return new Symbol(ParserSym.PF, yytext());}
{NL}		{ Main.print("nl:"+new Noeud("CST",yytext())+"#");return new Symbol(ParserSym.NL, yytext());}
{EQ}			{ Main.print("eq:"+new Noeud("CST",yytext())+"#");return new Symbol(ParserSym.EQ, new Noeud(yytext()));}
{VRG}			{ Main.print("vrg:"+new Noeud("CST",yytext())+"#");return new Symbol(ParserSym.VRG, yytext());}
{VOID}			{ Main.print("void:"+new Noeud("CST",yytext())+"#");return new Symbol(ParserSym.VOID, yytext());}
{RETURN}			{ Main.print("return:"+new Noeud("CST",yytext())+"#");return new Symbol(ParserSym.RETURN, new Noeud("RET"));}
{TYPE}			{ Main.print("type:"+new Noeud("CST",yytext())+"#");return new Symbol(ParserSym.TYPE, yytext());}
{OPBOOL}			{ Main.print("opbool:"+new Noeud("CST",yytext())+"#");return new Symbol(ParserSym.OPBOOL, new Noeud(yytext()));}
{OP}			{ Main.print("op:"+new Noeud("CST",yytext())+"#");return new Symbol(ParserSym.OP, new Noeud(yytext()));}
{VAR}         { Main.print("var:"+new Noeud("CST",yytext())+"#");return new Symbol(ParserSym.VAR, new Noeud("variable",yytext()));}
{NUM}         { Main.print("num:"+new Noeud("CST",yytext())+"#");return new Symbol(ParserSym.NUM, new Noeud("CST",yytext()));}
{COM}         { Main.print("com:"+new Noeud("CST",yytext())+"#");return new Symbol(ParserSym.COM);}
