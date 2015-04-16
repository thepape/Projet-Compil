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

NUM = [0-9]+

/* 
NUM	= (" "|\t)*[0-9]+(" "|\t)* 
*/

OPBOOL = (" "|\t)*(==|<|>|<=|>=|=\!|or|OR|and|AND|\!)(" "|\t)*
MUL = (" "|\t)*(\*|\/)(" "|\t)*
ADD = (" "|\t)*(\+|-)(" "|\t)*
INT = (" "|\t)*(int|INT)(" "|\t)*
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

{ELSE}			{ Main.print("else:"+yytext()+"#");return new Symbol(ParserSym.ELSE, new Noeud("ELSE"));}
{IF}         { Main.print("if:"+yytext()+"#");return new Symbol(ParserSym.IF, new Noeud("IF"));}
{WHILE}         { Main.print("while:"+yytext()+"#");return new Symbol(ParserSym.WHILE, new Noeud("WHILE"));}

{PV}         { Main.print("pv:"+yytext()+"#");return new Symbol(ParserSym.PV, yytext());}
{AO}         { Main.print("ao:"+yytext()+"#");return new Symbol(ParserSym.AO, yytext());}
{AF}         { Main.print("af:"+yytext()+"#");return new Symbol(ParserSym.AF, yytext());}
{PO}         { Main.print("po:"+yytext()+"#");return new Symbol(ParserSym.PO, yytext());}
{PF}         { Main.print("pf:"+yytext()+"#");return new Symbol(ParserSym.PF, yytext());}
{NL}		{ Main.print("nl:"+yytext()+"#");return new Symbol(ParserSym.NL, yytext());}
{EQ}			{ Main.print("eq:"+yytext()+"#");return new Symbol(ParserSym.EQ, yytext());}
{VRG}			{ Main.print("vrg:"+yytext()+"#");return new Symbol(ParserSym.VRG, yytext());}
{VOID}			{ Main.print("void:"+yytext()+"#");return new Symbol(ParserSym.VOID, yytext());}
{RETURN}			{ Main.print("return:"+yytext()+"#");return new Symbol(ParserSym.RETURN);}
{INT}			{ Main.print("type:"+yytext()+"#");return new Symbol(ParserSym.INT, yytext());}
{OPBOOL}			{ Main.print("opbool:"+yytext()+"#");return new Symbol(ParserSym.OPBOOL, yytext());}
{MUL}			{ Main.print("op:"+yytext()+"#");return new Symbol(ParserSym.MUL, yytext());}
{ADD}			{ Main.print("op:"+yytext()+"#");return new Symbol(ParserSym.ADD, yytext());}
{VAR}         { Main.print("var:"+yytext()+"#");return new Symbol(ParserSym.VAR, yytext());}
{NUM}         { Main.print("num:"+yytext()+"#");return new Symbol(ParserSym.NUM, Integer.parseInt(Main.virerEspaces(yytext())));}
{COM}         { Main.print("com:"+yytext()+"#");return new Symbol(ParserSym.COM);}
