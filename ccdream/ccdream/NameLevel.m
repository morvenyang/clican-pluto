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

#import "NameLevel.h"


@implementation NameLevel

NSCharacterSet *nameInternalSet;

@synthesize name, nextLevel;

- (BOOL)isKey
{
	if (path == PATH_LEVEL_KEY) {
		return YES;
	}
	else {
		return NO;
	}
}

- (id)setLeaf 
{
	id value;
	
	if (nextLevel) {
		id leaf = [nextLevel setLeaf];
		if ([nextLevel isKey]) {
			value = [[NSMutableDictionary alloc] init];
			[value setObject:leaf forKey:nextLevel.name];
		} else {
			value = [[NSMutableArray alloc] init];
			[value addObject:leaf];
		}
	} else {
		value = [[NSMutableString alloc] init];
	}
	
	return value;
}

- (id)getLeaf:(id)Data 
   autoCreate:(BOOL)autoCreate
{
	id value;
	
	if ([self isKey]) {
		
		if (![Data isKindOfClass:[NSDictionary class]]) {
			return nil;
		}
		
		//NSLog(@"name:%@", name);
		
		value = [(NSMutableDictionary *)Data valueForKey:name];
		
		//! fix bug here! 2011-11-14
		//!		must check NSNull
		if ((value == nil) || ([value isKindOfClass:[NSNull class]])) { 
			if (!autoCreate) {
				return nil;
			}
			value = [self setLeaf];
			[(NSMutableDictionary *)Data setObject:value forKey:name];
		} else {
			if (!nextLevel) { //! add this for outer dictionary can hold. by evan. 14.Sep.2011
				[value retain];
			}
		}

	} else {
		
		if (![Data isKindOfClass:[NSArray class]]) {
			return nil;
		}
		
		if (index >= [(NSArray *)Data count]) {
			if (!autoCreate) {
				return nil;
			}
			value = [self setLeaf];
			[(NSMutableArray *)Data addObject:value];
		} else {
			value = [(NSMutableArray *)Data objectAtIndex:index];
			if (value == nil) {
				if (!autoCreate) {
					return nil;
				}
				value = [self setLeaf];
				[(NSMutableArray *)Data addObject:value];
			} else {
				if (!nextLevel) { //! add this for outer dictionary can hold. by evan. 14.Sep.2011
					[value retain];
				}
			}
		}
	}
	
	if (!nextLevel) {
		return value; 
	} else {
		return [nextLevel getLeaf:value autoCreate:autoCreate];
	}
}

- (id)initWithName:(NSString *)n
		  withPath:(PathLevelType)p
{
	self = [super init];
	if (!self) {
		return self;
	}
	
	path = p;
	
	if (!nameInternalSet) {
		nameInternalSet = [NSCharacterSet characterSetWithCharactersInString:@".[]"];
		[nameInternalSet retain];
	}
	
	unichar t;
	
	int offset = [n rangeOfCharacterFromSet:nameInternalSet].location;
	if (offset > [n length]) {
		
		offset = n.length;
		
		t = 0;
	}
	else {
		t = [n characterAtIndex:offset];
		
		if (path == PATH_LEVEL_INDEX) {
			
			if (t == ']') {
				
				if (offset < (n.length-1)) {
					t = [n characterAtIndex:offset+1];
				} else {
					t = 0;
				}
			} else {
				
				//! error here!
				return self;
			}
		}
	}
	
	name = [n substringToIndex:offset];
	[name retain];
	if (path == PATH_LEVEL_INDEX) {
		index = [name intValue];
		offset ++;
	}
	
	switch (t) {
		case 0:
			
			nextLevel = nil;
			
			return self;
			
		case '.':
			
			nextLevel = [[NameLevel alloc] initWithName:[n substringFromIndex:offset + 1]
											   withPath:PATH_LEVEL_KEY];
			
			return self;
			
		case '[':
			
			nextLevel = [[NameLevel alloc] initWithName:[n substringFromIndex:offset + 1]
											   withPath:PATH_LEVEL_INDEX];
			
			return self;
			
		default:
			//! error !
			nextLevel = nil;
			
			return self;
	}		
	
	return self;
}

- (void) print:(NSString *)prompt
{
	NSString *nextPrompt = [[NSString alloc] initWithFormat:@"%@   ", prompt];
	NSLog(@"%@<LEVEL>\n",prompt);
	if (path == PATH_LEVEL_KEY) {
		NSLog(@"%@<KEY>%@</KEY>\n",nextPrompt,name);
	} else {
		NSLog(@"%@<INDEX>%d</INDEX>\n",nextPrompt,index);
	}

	[nextLevel print:nextPrompt];
	NSLog(@"%@</LEVEL>\n",prompt);
	[nextPrompt release];
}

- (void)dealloc {
	[name release];
	[nextLevel release];
    [super dealloc];
}

@end
