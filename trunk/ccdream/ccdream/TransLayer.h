/*
 Copyright [2011] [chunyi zhou]
 
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 
 http://www.apache.org/licenses/LICENSE-2.0
 
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

#import <Foundation/Foundation.h>

extern void initWithTemplateFile(NSString *filename, id iv);
extern void initLex(NSString *strTemplate, id iv);

extern int yyparse(void);

extern int createString(char *text);
extern void printStatements();

extern int createEntry(int pStatement); 
extern int createStatement(int pBlock);

extern int createTextBlock(int pText);
extern int createNumberBlock(int pNumber);
extern int createNameBlock(int pName);

extern int mergeTextStatement(int pText, int pStatement);
extern int mergeNumberStatement(int pNumber, int pStatement);
extern int mergeCommandStatement(int pCommandBlock, int pStatement);
extern int mergeShowValueStatement(int pNameBlock, int pStatement);

extern int createSetCommand(int pName, int pExpBlock);
extern int createIfCommand(int pExpBlock, int pStatement, int pElseStatement);
extern int createForeachCommand(int pName, int pNameLib, int pStatement);

extern int createExpAddBlock(int pExpBlock, int pNumber);
extern int createExpSubBlock(int pExpBlock, int pNumber);