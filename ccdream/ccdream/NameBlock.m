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

#import "NameBlock.h"

@implementation NameBlock

NSMutableDictionary *GlobaNameDic;

+ (ValueType)checkValueType:(id)value
{
	if ([value isKindOfClass:[NSString class]]) { //! new create is always string type
		return VALUE_STRING;
	}
	
	if ([value isKindOfClass:[NSNumber class]]) {
		return VALUE_INTEGER;
	}
	
	if ([value isKindOfClass:[NSDictionary class]]) {
		return VALUE_DIC;
	}
	
	if ([value isKindOfClass:[NSArray class]]) {
		return VALUE_ARRAY;
	}
	
	return VALUE_ANY;
}

- (id) initWithName:(NSString *)n
{
	if (self = [super init]) {
		
		//! complex here
		name = [[NSString alloc] initWithString:n];
		
		nameLevel = [[NameLevel alloc] initWithName:n 
										   withPath:PATH_LEVEL_KEY];
	}
	return self;
}



- (void) initNameWithData:(NSMutableDictionary *)dictionaryData 
{
	/*if ([name compare:@"factory_info.factory_size"] == 0) {
		NSLog(@"break here");
	}*/
	
	if (value) {
		id newValue = [nameLevel getLeaf:dictionaryData autoCreate:NO];
		
		if (newValue) {
			//! replace old value
			
			[value release];
			
			value = newValue;
			
			[value retain];
			
			return;
		} else {
			
			return; //! use old value.
		}
	} 
	
	value = [nameLevel getLeaf:dictionaryData autoCreate:YES];
	
	if (value == nil) {
		//! for invalid name, create internal data for use.
		value = [[[NSMutableString alloc] init] autorelease];
		valueType = VALUE_STRING;
		return;
	}
	
	valueType = [NameBlock checkValueType:value];
}

- (ValueType) getValueType
{
	return valueType;
}

- (void) print:(NSString *)prompt
{
	NSLog(@"%@<NAME>%@</NAME>\n",prompt,name);
}

- (RenderStatus)renderBlockWithData:(NSMutableDictionary *)dictionaryData 
					   returnString:(NSMutableString *)strResult;
{	
    [self initNameWithData:dictionaryData];
    
	if ([self getValueType] == VALUE_STRING) {
		[strResult appendString:value];
	}
	
	if ([self getValueType] == VALUE_INTEGER) {
		[strResult appendFormat:@"%d", [(NSNumber *)value intValue]];
	}
	
	return RENDER_OK;
}

- (NSInteger) getIntegerValue
{
	switch (valueType) {
		case VALUE_INTEGER:
			return [(NSNumber *)value intValue];
		default:
			return 0;
	}
}

- (void) getStringValue:(NSMutableString *)strResult
{
	switch (valueType) {
		case VALUE_STRING:
			[strResult initWithString:(NSString *)value];
			return;
		case VALUE_INTEGER:
			[strResult initWithFormat:@"%d", [(NSNumber *)value intValue]];
			return;
		default:
			return;
	}
}

- (void) setIntegerValue:(NSInteger) v
{
	switch (valueType) {
		case VALUE_INTEGER:
			[(NSNumber *)value initWithInt:v];
			return;
		case VALUE_STRING:
			[(NSMutableString *)value appendFormat:@"%d", v];
			return;
		default:
			return;
	}
}

- (void) setStringValue:(NSString *) v
{
	switch (valueType) {
		case VALUE_STRING:
			[(NSMutableString *)value setString:v];
			return;
		case VALUE_INTEGER:
			//! don't work
			return;
		default:
			return;
	}
}

- (void) setDictionary:(NSDictionary *) v
{
    // BUGFIX: use prefix but not whole text!!! by evan joe 2012.5.25
    [value setDictionary:v];
}

- (void)dealloc
{
	[name release];
	[nameLevel release];
	[super dealloc];
}

- (id) getObject
{
	return value;
}

- (void) reset
{
	[value release];
	value = nil;
	/*
	ValueType type = [NameBlock checkValueType:value];
	if (type != VALUE_ANY) {
		[value release];
		value = nil;
	}
	 */
}

- (NSString *) getNamePrefix
{
    return nameLevel.name;
}

@end
