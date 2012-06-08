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

#import <UIKit/UIKit.h>
#import "iVelocity.h"

#pragma mark Block Interface

typedef enum {
	VALUE_ANY = 0,
	VALUE_INTEGER,	
	VALUE_STRING,
	VALUE_DIC,
	VALUE_ARRAY
} ValueType;

@protocol Block

- (void) print:(NSString *)prompt;

@end


@protocol Render

- (RenderStatus)renderBlockWithData:(NSMutableDictionary *)dictionaryData 
					   returnString:(NSMutableString *)strResult;

@end

@protocol Expression

- (NSInteger) getIntegerValue;
- (void) getStringValue:(NSMutableString *)strResult;
- (ValueType) getValueType;

@end

@protocol Name

- (void) initNameWithData:(NSMutableDictionary *)dictionaryData;
- (void) setIntegerValue:(NSInteger) v;
- (void) setStringValue:(NSString *) v;
- (id) getObject;

//! add by EvanJoe 2012.5.25
- (void) setDictionary:(NSDictionary *) v;

@end



