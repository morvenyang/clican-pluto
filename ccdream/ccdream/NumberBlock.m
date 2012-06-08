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

#import "NumberBlock.h"


@implementation NumberBlock

- (id) initWithString:(NSString *)textValue
{
	if (self = [super init]) {
		
		//! complex here, add float here in future.
		
		intValue = [textValue intValue];
	}
	return self;
}

- (ValueType) getValueType
{
	return VALUE_INTEGER;
}

- (void) print:(NSString *)prompt
{
	NSLog(@"%@<NUMBER>%d</NUMBER>\n",prompt,intValue);
}

- (NSInteger) getIntegerValue
{
	return intValue;
}

- (void) getStringValue:(NSMutableString *)strResult
{
	[strResult appendFormat:@"%d", intValue];
}

- (void)dealloc
{
	[super dealloc];
}

@end
