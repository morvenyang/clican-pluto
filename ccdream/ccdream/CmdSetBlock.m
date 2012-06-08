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

#import "CmdSetBlock.h"


@implementation CmdSetBlock

- (id) initWithName:(NameBlock *)name_block withExp:(id)exp
{
	if (self = [super init]) {		
		nameBlock = name_block;
		expressionBlock = exp;
	}
	return self;
}

- (void) print:(NSString *)prompt
{
	NSString *nextPrompt = [[NSString alloc] initWithFormat:@"%@   ", prompt];
	NSLog(@"%@<SET>\n",prompt);
	[nameBlock print:nextPrompt];
	[expressionBlock print:nextPrompt];
	NSLog(@"%@</SET>\n",prompt);
	[nextPrompt release];
}

#pragma mark render interface

- (RenderStatus)renderBlockWithData:(NSMutableDictionary *)dictionaryData 
					   returnString:(NSMutableString *)strResult;
{
	[nameBlock initNameWithData:dictionaryData];
	
	NSMutableString *str = [[NSMutableString alloc] init];
	
	switch ([expressionBlock getValueType]) {
		case VALUE_STRING:
			[expressionBlock getStringValue:str];
			[nameBlock setStringValue:str];
			break;
		case VALUE_INTEGER:
			[nameBlock setIntegerValue:[expressionBlock getIntegerValue]];
			break;
		default:
			break;
	}
	
	[str release];
	
	return RENDER_OK;
}

- (void)dealloc
{
	[super dealloc];
}

@end
