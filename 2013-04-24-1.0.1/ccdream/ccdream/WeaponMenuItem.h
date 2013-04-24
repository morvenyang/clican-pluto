//
//  WeaponMenuItem.h
//  ccdream
//
//  Created by wei zhang on 12-6-13.
//  Copyright 2012年 Clican. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "cocos2d.h"

@interface WeaponMenuItem : CCMenuItemImage {
    CCNode<CCLabelProtocol, CCRGBAProtocol>* _label;
    ccColor3B        colorBackup;
    ccColor3B        _disabledColor;
}

/** the color that will be used to disable the item */
@property (nonatomic,readwrite) ccColor3B disabledColor;

/** Label that is rendered. It can be any CCNode that implements the CCLabelProtocol */
@property (nonatomic,readwrite,assign) CCNode<CCLabelProtocol, CCRGBAProtocol>* label;

+ (id) itemWithLabel:(CCNode<CCLabelProtocol,CCRGBAProtocol>*)label normalImage:(NSString*)value target:(id) target selector:(SEL) s;

/** creates a menu item with a label and a normal and selected image*/
+ (id) itemWithLabel:(CCNode<CCLabelProtocol,CCRGBAProtocol>*)label normalImage:(NSString*)value selectedImage:(NSString*) value2;
/** creates a menu item with a label and a normal and selected image with target/selector */
+(id) itemFromLabel:(CCNode<CCLabelProtocol,CCRGBAProtocol>*)label normalImage: (NSString*)value selectedImage:(NSString*) value2 target:(id) r selector:(SEL) s;
/** creates a menu item with a label and a normal,selected  and disabled image */
+(id) itemFromLabel:(CCNode<CCLabelProtocol,CCRGBAProtocol>*)label normalImage: (NSString*)value selectedImage:(NSString*) value2 disabledImage: (NSString*) value3;
/** creates a menu item with a label and a normal,selected  and disabled image with target/selector */
+(id) itemFromLabel:(CCNode<CCLabelProtocol,CCRGBAProtocol>*)label normalImage: (NSString*)value selectedImage:(NSString*) value2 disabledImage:(NSString*) value3 target:(id) r selector:(SEL) s;
/** initializes a menu item with a label and a normal, selected  and disabled image with target/selector */
-(id) initFromLabel:(CCNode<CCLabelProtocol,CCRGBAProtocol>*)label normalImage: (NSString*) value selectedImage:(NSString*)value2 disabledImage:(NSString*) value3 target:(id) r selector:(SEL) s;
#if NS_BLOCKS_AVAILABLE
/** creates a menu item with a label and a normal and selected image with a block.
 The block will be "copied".
 */
+(id) itemFromLabel:(CCNode<CCLabelProtocol,CCRGBAProtocol>*)label normalImage: (NSString*)value selectedImage:(NSString*) value2 block:(void(^)(id sender))block;
/** creates a menu item with a label and a normal,selected  and disabled image with a block.
 The block will be "copied".
 */
+(id) itemFromLabel:(CCNode<CCLabelProtocol,CCRGBAProtocol>*)label normalImage: (NSString*)value selectedImage:(NSString*) value2 disabledImage:(NSString*) value3 block:(void(^)(id sender))block;
/** initializes a menu item with a label and a normal, selected  and disabled image with a block.
 The block will be "copied".
 */
-(id) initFromLabel:(CCNode<CCLabelProtocol,CCRGBAProtocol>*)label normalImage: (NSString*) value selectedImage:(NSString*)value2 disabledImage:(NSString*) value3 block:(void(^)(id sender))block;
#endif

/** sets a new string to the inner label */
-(void) setString:(NSString*)label;

/** Enable or disabled the CCMenuItemFont
 @warning setIsEnabled changes the RGB color of the font
 */
-(void) setIsEnabled: (BOOL)enabled;
@end
