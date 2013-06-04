//
//  TargetButton.h
//  appletvserver
//
//  Created by zhang wei on 13-6-4.
//
//

#import <Three20UI/Three20UI.h>

@interface TargetButton : TTButton{
    id _target;
}
@property (nonatomic, retain) id target;
+ (TargetButton*)buttonWithStyle:(NSString*)selector title:(NSString*)title target:(id) target;
@end
