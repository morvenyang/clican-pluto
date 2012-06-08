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

#import "StatementBlock.h"
#import "TextBlock.h"

@implementation StatementBlock

- (id) initWithBlock:(id)block
{
	if (self = [super init]) {
		blockArray = [[NSMutableArray alloc] init];
		[blockArray addObject:block];
	}
	return self;
}

- (void) addLeft:(id)block
{
	if ([block isKindOfClass:[TextBlock class]]) {
		id oldblock = [blockArray objectAtIndex:0];
		if ([oldblock isKindOfClass:[TextBlock class]]) {
			[oldblock AppendText:[block getText]];
			[block release];
			return;
		}
	}
	[blockArray insertObject:block atIndex:0];
}

- (void) print:(NSString *)prompt
{
	NSString *nextPrompt = [[NSString alloc] initWithFormat:@"%@   ", prompt];
	NSLog(@"%@<Statement>\n",prompt);
	for (int i=0; i<blockArray.count; i++) {
		id block = [blockArray objectAtIndex:i];
		
		[block print:nextPrompt];
	}
	NSLog(@"%@</Statement>\n",prompt);
	[nextPrompt release];
}

#pragma mark Block Interface

- (RenderStatus)renderBlockWithData:(NSMutableDictionary *)dictionaryData 
					   returnString:(NSMutableString *)strResult;
{
	
	for (int i=0; i<blockArray.count; i++) {
		id block = [blockArray objectAtIndex:i];
		
		RenderStatus status = [block renderBlockWithData:dictionaryData
											returnString:strResult];
		if (status != RENDER_OK) {
			return status;
		}
	}
	
	return RENDER_OK;
}

- (void)dealloc
{
	[blockArray removeAllObjects];
	[blockArray release];
	[super dealloc];
}

@end
