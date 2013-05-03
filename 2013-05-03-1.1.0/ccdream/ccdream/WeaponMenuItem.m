//
//  WeaponMenuItem.m
//  ccdream
//
//  Created by wei zhang on 12-6-13.
//  Copyright 2012年 Clican. All rights reserved.
//

#import "WeaponMenuItem.h"


@implementation WeaponMenuItem

@synthesize disabledColor = disabledColor_;

+ (id) itemWithLabel:(CCNode<CCLabelProtocol,CCRGBAProtocol>*)label normalImage:(NSString*)value selectedImage:(NSString*) value2
{
    return [self itemFromLabel:label normalImage:value selectedImage:value2 disabledImage:nil target:nil selector:nil];
}

+ (id) itemWithLabel:(CCNode<CCLabelProtocol,CCRGBAProtocol>*)label normalImage:(NSString*)value target:(id) target selector:(SEL) s
{
    return [self itemFromLabel:label normalImage:value selectedImage:nil disabledImage:nil target:target selector:s];
}

+(id) itemFromLabel:(CCNode<CCLabelProtocol,CCRGBAProtocol>*)label normalImage: (NSString*)value selectedImage:(NSString*) value2 target:(id) r selector:(SEL) s
{
    return [self itemFromLabel:label normalImage:value selectedImage:value2 disabledImage:nil target:r selector:s];
}

+(id) itemFromLabel:(CCNode<CCLabelProtocol,CCRGBAProtocol>*)label normalImage: (NSString*)value selectedImage:(NSString*) value2 disabledImage: (NSString*) value3
{
    return [[[self alloc] initFromLabel:label normalImage:value selectedImage:value2 disabledImage:value3 target:nil selector:nil] autorelease];
}

+(id) itemFromLabel:(CCNode<CCLabelProtocol,CCRGBAProtocol>*)label normalImage: (NSString*)value selectedImage:(NSString*) value2 disabledImage:(NSString*) value3 target:(id) r selector:(SEL) s
{
    return [[[self alloc] initFromLabel:label normalImage:value selectedImage:value2 disabledImage:value3 target:r selector:s] autorelease];
}

-(id) initFromLabel:(CCNode<CCLabelProtocol,CCRGBAProtocol>*)label normalImage: (NSString*) value selectedImage:(NSString*)value2 disabledImage:(NSString*) value3 target:(id) r selector:(SEL) s
{
    if ((self = [super initFromNormalImage:value selectedImage:value2 disabledImage:value3 target:r selector:s])) {
        colorBackup = ccWHITE;
        disabledColor_ = ccWHITE;
        self.label = label;
    }
    return self;
}

#if NS_BLOCKS_AVAILABLE

+(id) itemFromLabel:(CCNode<CCLabelProtocol,CCRGBAProtocol>*)label normalImage: (NSString*)value selectedImage:(NSString*) value2 block:(void(^)(id sender))block
{
    return [self itemFromLabel:label normalImage:value selectedImage:value2 disabledImage:nil block:block];
}

+(id) itemFromLabel:(CCNode<CCLabelProtocol,CCRGBAProtocol>*)label normalImage: (NSString*)value selectedImage:(NSString*) value2 disabledImage:(NSString*) value3 block:(void(^)(id sender))block
{
    return [[[self alloc] initFromLabel:label normalImage:value selectedImage:value2 disabledImage:value3 block:block] autorelease];
}

-(id) initFromLabel:(CCNode<CCLabelProtocol,CCRGBAProtocol>*)label normalImage: (NSString*) value selectedImage:(NSString*)value2 disabledImage:(NSString*) value3 block:(void(^)(id sender))block
{
    block_ = [block copy];
    return [self initFromLabel:label normalImage:value selectedImage:value2 disabledImage:value3 target:block_ selector:@selector(ccCallbackBlockWithSender:)];
}
#endif // NS_BLOCKS_AVAILABLE

-(CCNode<CCLabelProtocol, CCRGBAProtocol>*) label
{
    return _label;
}
-(void) setLabel:(CCNode<CCLabelProtocol, CCRGBAProtocol>*) label
{
    if( label != _label ) {
        [self removeChild:_label cleanup:YES];
        [self addChild:label];
        
        _label = label;
        
        [self repositionLabel];
    }
}

-(void) setString:(NSString *)string
{
    [_label setString:string];
    [self repositionLabel];
}

-(void) setPosition:(CGPoint)position
{
    [super setPosition:position];
    [self repositionLabel];
}

-(void) repositionLabel
{
    _label.position = ccp(normalImage_.position.x + normalImage_.contentSize.width*2, normalImage_.position.y + normalImage_.contentSize.height/2);
}

-(void) selected
{
    // subclass to change the default action
    if(isEnabled_) {
        [super selected];
        // Move the label down 1 point to look like the button's pressed.
        _label.position = ccp(_label.position.x, _label.position.y-1);
    }
}

-(void) unselected
{
    // subclass to change the default action
    if(isEnabled_) {
        [super unselected];
        // Move the label back up
        _label.position = ccp(_label.position.x, _label.position.y+1);
    }
}

-(void) setIsEnabled: (BOOL)enabled
{
    if( isEnabled_ != enabled ) {
        if(enabled == NO) {
            colorBackup = [_label color];
            [_label setColor: disabledColor_];
        }
        else
            [_label setColor:colorBackup];
    }
    
    [super setIsEnabled:enabled];
}

- (void) setOpacity: (GLubyte)opacity
{
    [_label setOpacity:opacity];
}
-(GLubyte) opacity
{
    return [_label opacity];
}
-(void) setColor:(ccColor3B)color
{
    [_label setColor:color];
}
-(ccColor3B) color
{
    return [_label color];
}

@end
