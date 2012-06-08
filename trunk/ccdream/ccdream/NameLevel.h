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
#import "CoreInterface.h"

typedef enum {				//! for example, a.b[2].c	d.e[3]
	PATH_LEVEL_KEY = 0,		//!	             a,b,c		d,e
	PATH_LEVEL_INDEX		//!					[2]		   [3]
} PathLevelType;


@interface NameLevel : NSObject<Block> {
	PathLevelType path;
	NSString *name;		//! only for NAME_LEVEL_KEY
	NSUInteger index;	//! only for NAME_LEVEL_INDEX
	
	//! not support yet !
	NSUInteger bits;	//! only for NAME_LEVEL_STRING, get display bits for iOS, when 0 means all.
	
	NameLevel *nextLevel; //! nil means leaf. 
}

@property (nonatomic, retain) NSString *name;
@property (nonatomic, assign) NameLevel *nextLevel;

- (id)getLeaf:(id)Data autoCreate:(BOOL)autoCreate;

- (id)initWithName:(NSString *)n withPath:(PathLevelType)p;

@end
