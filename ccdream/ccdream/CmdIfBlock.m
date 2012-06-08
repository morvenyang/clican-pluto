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

#import "CmdIfBlock.h"


@implementation CmdIfBlock

- (id) initWithExp:(id)expBlock 
		  withThen:(StatementBlock *)pStatement 
		  withElse:(StatementBlock *)pElseStatement
{
	if (self = [super init]) {		
		expressionBlock = expBlock;
		thenStatement = pStatement;
		elseStatement = pElseStatement;
	}
	return self;
}

- (void) print:(NSString *)prompt
{
	NSString *nextPrompt = [[NSString alloc] initWithFormat:@"%@   ", prompt];
	NSLog(@"%@<IF>\n",prompt);
	[expressionBlock print:nextPrompt];
	[thenStatement print:nextPrompt];
	[elseStatement print:nextPrompt];
	NSLog(@"%@</IF>\n",prompt);
	[nextPrompt release];
}

#pragma mark render interface

- (RenderStatus)renderBlockWithData:(NSMutableDictionary *)dictionaryData 
					   returnString:(NSMutableString *)strResult;
{
	if ([expressionBlock getIntegerValue]) {
		if(thenStatement) {
			return [thenStatement renderBlockWithData:dictionaryData 
										 returnString:strResult];
		}
	} else {
		if(elseStatement) {
			return [elseStatement renderBlockWithData:dictionaryData 
										 returnString:strResult];
		}
	}
	
	return RENDER_OK;
}

- (void)dealloc
{
	[expressionBlock release];
	[thenStatement release];
	[elseStatement release];
	[super dealloc];
}

@end
