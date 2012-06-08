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

#import "CmdForeachBlock.h"


@implementation CmdForeachBlock

- (id) initWithName:(NameBlock *)n 
		withNameLib:(NameBlock *)nl
	  withStatement:(StatementBlock *)s;
{
	if (self = [super init]) {		
		name = n;
		nameLib = nl;
		statement = s;
	}
	return self;
}

- (void) print:(NSString *)prompt
{
	NSString *nextPrompt = [[NSString alloc] initWithFormat:@"%@   ", prompt];
	NSLog(@"%@<FOREACH>\n",prompt);
	[name print:nextPrompt];
	[nameLib print:nextPrompt];
	[statement print:nextPrompt];
	NSLog(@"%@</FOREACH>\n",prompt);
	[nextPrompt release];
}



#pragma mark render interface

- (RenderStatus)renderBlockWithData:(NSMutableDictionary *)dictionaryData 
					   returnString:(NSMutableString *)strResult;
{
	NSArray *keyArray;
	
    [name initNameWithData:dictionaryData];
	[nameLib initNameWithData:dictionaryData];
	
	switch ([nameLib getValueType]) {
		case VALUE_DIC:
			keyArray = [[nameLib getObject] allValues];
			break;
		case VALUE_ARRAY:
			keyArray = [nameLib getObject];
			break;
		default:
			return RENDER_ERROR;
	}
	
	for(int i=0;i<keyArray.count;i++) {
		
		id value = [keyArray objectAtIndex:i];
		
		ValueType type = [NameBlock checkValueType:value];
		
		switch (type) {
			case VALUE_STRING:
                [name setStringValue:value];
				break;
			case VALUE_INTEGER:
                [name setIntegerValue:[value intValue]];
				break;
            case VALUE_DIC: 
                // BUGFIX: use prefix but not whole text!!! by evan joe 2012.5.25
                [name setDictionary:value];
                break;
            case VALUE_ARRAY:
            case VALUE_ANY:
			default:
				return RENDER_ERROR;
		}
		
		RenderStatus res = [statement renderBlockWithData:dictionaryData 
											 returnString:strResult];
		
		if (res == RENDER_ERROR) {
			return RENDER_ERROR;
		}
	}
	
	return RENDER_OK;
}

- (void)dealloc
{
	[statement release];
	[super dealloc];
}


@end
