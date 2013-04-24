//
//  AtvOneLineMenuItem.h
//  appletvserver
//
//  Created by zhang wei on 13-3-25.
//
//

#import <Foundation/Foundation.h>

@interface AtvOneLineMenuItem : NSObject{
    NSString* _onPlay;
    NSString* _onSelect;
    NSString* _label;
    NSString* _image;
}
@property (nonatomic, copy) NSString* onPlay;
@property (nonatomic, copy) NSString* onSelect;
@property (nonatomic, copy) NSString* label;
@property (nonatomic, copy) NSString* image;
@end
